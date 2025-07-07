package com.example.diary.presentation.combined.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.data.mapper.toUi
import com.example.diary.domain.repository.StressLogRepository
import com.example.diary.domain.usecase.GetAllWorkoutTypesUseCase
import com.example.diary.domain.usecase.GetWorkoutSessionsForDateUseCase
import com.example.diary.domain.usecase.meal.GetAllMealsWithDetailsUseCase
import com.example.diary.domain.usecase.medication.medicationLog.GenerateMedicationLogsForDateUseCase
import com.example.diary.domain.usecase.medication.medicationLog.GetMedicationLogsForDateUseCase
import com.example.diary.domain.usecase.sleep.GetSleepSessionsForDateUseCase
import com.example.diary.domain.usecase.symptom.GetAllSymptomTypesUseCase
import com.example.diary.domain.usecase.symptom.GetAllSymptomsUseCase
import com.example.diary.domain.usecase.water.GetAllBeverageCategoriesUseCase
import com.example.diary.domain.usecase.water.GetWaterIntakesForDateUseCase
import com.example.diary.presentation.combined.calendar.CombinedCalendarFilterViewModel
import com.example.diary.presentation.combined.calendar.CombinedCalendarFilterViewModel.CombinedEntryDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import java.time.Duration

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getAllMealsUseCase: GetAllMealsWithDetailsUseCase,
    private val getAllSymptomsUseCase: GetAllSymptomsUseCase,
    private val getAllSymptomTypesUseCase: GetAllSymptomTypesUseCase,
    private val generateMedicationLogsForDateUseCase: GenerateMedicationLogsForDateUseCase,
    private val getMedicationLogsForDateUseCase: GetMedicationLogsForDateUseCase,
    private val getWaterIntakesUseCase: GetWaterIntakesForDateUseCase,
    private val getAllBeverageCategoriesUseCase: GetAllBeverageCategoriesUseCase,
    private val getAllWorkoutTypesUseCase: GetAllWorkoutTypesUseCase,
    private val getWorkoutSessionsUseCase: GetWorkoutSessionsForDateUseCase,
    private val getSleepSessionsUseCase: GetSleepSessionsForDateUseCase,
    private val stressLogRepository: StressLogRepository
) : ViewModel() {

    private val _weekStart = MutableStateFlow(
        LocalDate.now().with(DayOfWeek.MONDAY)
    )
    val weekStart: StateFlow<LocalDate> = _weekStart.asStateFlow()

    private val _entries =
        MutableStateFlow<List<CombinedEntryDetail>>(emptyList())
    private val entries: StateFlow<List<CombinedEntryDetail>> = _entries.asStateFlow()

    val workoutsByDay: StateFlow<Map<LocalDate, List<String>>> = entries
        .map { list ->
            list.filterIsInstance<CombinedEntryDetail.Workout>()
                .groupBy  { it.dateTime.toLocalDate() }
                .mapValues { entry -> entry.value.map { w -> w.typeName } }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    val longestFastingByDay: StateFlow<Map<LocalDate, Double>> = entries
        .map { list ->

            val mealsByDay = list
                .filterIsInstance<CombinedEntryDetail.Meal>()
                .groupBy { it.dateTime.toLocalDate() }

            mealsByDay.mapValues { (_, meals) ->

                val times = meals
                    .map { it.dateTime }
                    .sorted()


                val gaps = times
                    .zipWithNext { prev, next ->
                        Duration.between(prev, next).toMinutes().toDouble() / 60.0
                    }


                gaps.maxOrNull() ?: 0.0
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    init {
        refreshEntries()
    }

    fun changeWeek(offset: Long) {
        _weekStart.value = _weekStart.value.plusWeeks(offset)
        refreshEntries()
    }

    val sleepByDay: StateFlow<Map<LocalDate, Double>> = entries
        .map { list ->
            list
                .filterIsInstance<CombinedEntryDetail.Sleep>()
                .groupBy { it.start.toLocalDate() }
                .mapValues { (_, sessions) ->

                    sessions.sumOf { s ->
                        ChronoUnit.MINUTES.between(s.start, s.end).toDouble()
                    } / 60.0
                }
        }
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyMap())

    val sleepQualityByDay: StateFlow<Map<LocalDate, Double>> = entries
        .map { list ->
            list.filterIsInstance<CombinedEntryDetail.Sleep>()
                .groupBy { it.start.toLocalDate() }
                .mapValues { (_, sessions) ->

                    sessions.mapNotNull { it.quality?.toDouble() }
                        .let { arr -> if (arr.isNotEmpty()) arr.average() else 0.0 }
                }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    private fun refreshEntries() = viewModelScope.launch {
        val start = _weekStart.value
        val end = start.plusDays(6)
        val dates = (0..6).map { start.plusDays(it.toLong()) }


        val meals = getAllMealsUseCase()
            .firstOrNull()
            .orEmpty()
            .map { it.toUi() }
            .filter { it.dateTime.toLocalDate() in start..end }
            .map { m -> CombinedEntryDetail.Meal(m.mealId, m.dateTime, m.name, m.method, m.ingredients) }


        val types = getAllSymptomTypesUseCase().firstOrNull().orEmpty()
        val symptoms = getAllSymptomsUseCase()
            .firstOrNull()
            .orEmpty()
            .filter { it.dateTime.toLocalDate() in start..end }
            .map { s ->
                val typeName = types.firstOrNull { it.typeId == s.typeId }?.name.orEmpty()
                CombinedEntryDetail.Symptom(s.symptomId, s.dateTime, typeName, s.intensity)
            }


        dates.forEach { generateMedicationLogsForDateUseCase(it) }
        val meds = dates.flatMap { date ->
            getMedicationLogsForDateUseCase(date)
                .firstOrNull().orEmpty()
                .filter { it.taken && it.scheduledTime.toLocalDate() == date }
                .map { log ->
                    CombinedEntryDetail.Medication(log.id, log.scheduledTime, log.name, log.dose, log.taken)
                }
        }


        val bevCats = getAllBeverageCategoriesUseCase().firstOrNull().orEmpty()
        val waters = dates.flatMap { date ->
            getWaterIntakesUseCase(date)
                .firstOrNull().orEmpty()
                .filter { it.timestamp.toLocalDate() == date }
                .map { w ->
                    val cat = bevCats.firstOrNull { it.categoryId == w.categoryId }?.name.orEmpty()
                    CombinedEntryDetail.Water(w.intakeId, w.timestamp, cat, w.amountMl)
                }
        }


        val sleeps = dates.flatMap { date ->
            getSleepSessionsUseCase(date)
                .firstOrNull().orEmpty()
                .filter { it.startTime.toLocalDate() == date }
                .map { s -> CombinedEntryDetail.Sleep(s.sessionId, s.startTime, s.startTime, s.endTime, s.quality) }
        }


        val workoutTypes = getAllWorkoutTypesUseCase().firstOrNull().orEmpty()
        val workouts = dates.flatMap { date ->
            getWorkoutSessionsUseCase(date)
                .firstOrNull().orEmpty()
                .filter { it.startTime.toLocalDate() == date }
                .map { w ->
                    val name = workoutTypes.firstOrNull { it.typeId == w.workoutTypeId }?.name.orEmpty()
                    CombinedEntryDetail.Workout(w.sessionId, w.startTime, name, w.intensity)
                }
        }

        val workoutsByDay: StateFlow<Map<LocalDate, List<String>>> = entries
            .map { list ->
                list.filterIsInstance<CombinedEntryDetail.Workout>()
                    .groupBy { it.dateTime.toLocalDate() }
                    .mapValues { it.value.map { w -> w.typeName } }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())


        val stresses = dates.flatMap { date ->
            stressLogRepository.getLogsForDate(date)
                .firstOrNull().orEmpty()
                .filter { it.timestamp.toLocalDate() == date }
                .map { st -> CombinedEntryDetail.Stress(st.logId, st.timestamp, st.level) }
        }

        _entries.value = meals + symptoms + meds + waters + sleeps + workouts + stresses
    }


    val totalSleepHours: StateFlow<Double> = entries.map { list ->
        list.filterIsInstance<CombinedEntryDetail.Sleep>()
            .sumOf { ChronoUnit.MINUTES.between(it.start, it.end).toDouble() } / 60.0
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, 0.0)

    val drinksByCategory: StateFlow<Map<String, Int>> = entries.map { list ->
        list.filterIsInstance<CombinedEntryDetail.Water>()
            .groupingBy { it.categoryName }
            .eachCount()
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyMap())

    val fluidByDay: StateFlow<Map<LocalDate, Int>> = entries.map { list ->
        list.filterIsInstance<CombinedEntryDetail.Water>()
            .groupBy { it.dateTime.toLocalDate() }
            .mapValues { it.value.sumOf { w -> w.amountMl } }
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyMap())

    val averageSleepQuality: StateFlow<Double> = entries.map { list ->
        list.filterIsInstance<CombinedEntryDetail.Sleep>()
            .mapNotNull { it.quality }
            .let { if (it.isNotEmpty()) it.average() else 0.0 }
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, 0.0)

    val maxStressByDay: StateFlow<Map<LocalDate, Int>> = entries.map { list ->
        list.filterIsInstance<CombinedEntryDetail.Stress>()
            .groupBy { it.dateTime.toLocalDate() }
            .mapValues { it.value.maxOfOrNull { s -> s.level } ?: 0 }
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyMap())

    val workoutsByCategory: StateFlow<Map<String, Int>> = entries.map { list ->
        list.filterIsInstance<CombinedEntryDetail.Workout>()
            .groupingBy { it.typeName }
            .eachCount()
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyMap())

    val avgWorkoutIntensity: StateFlow<Map<String, Double>> = entries.map { list ->
        list.filterIsInstance<CombinedEntryDetail.Workout>()
            .groupBy { it.typeName }
            .mapValues { it.value.map { w -> w.intensity.toDouble() }.average() }
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyMap())

    val mealsPerDay: StateFlow<Map<LocalDate, Int>> = entries.map { list ->
        list.filterIsInstance<CombinedEntryDetail.Meal>()
            .groupBy { it.dateTime.toLocalDate() }
            .mapValues { it.value.size }
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyMap())

    val symptomsByDay: StateFlow<Map<LocalDate, List<String>>> = entries.map { list ->
        list.filterIsInstance<CombinedEntryDetail.Symptom>()
            .groupBy { it.dateTime.toLocalDate() }
            .mapValues { it.value.map { s -> s.typeName } }
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyMap())

    val symptomIntensityByType: StateFlow<Map<String, Double>> = entries.map { list ->
        list.filterIsInstance<CombinedEntryDetail.Symptom>()
            .groupBy { it.typeName }
            .mapValues { it.value.map { s -> s.intensity.toDouble() }.average() }
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyMap())

    val medicationsTaken: StateFlow<Map<LocalDate, List<String>>> = entries.map { list ->
        list.filterIsInstance<CombinedEntryDetail.Medication>()
            .groupBy { it.dateTime.toLocalDate() }
            .mapValues { it.value.map { m -> m.name } }
    }.stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyMap())


}