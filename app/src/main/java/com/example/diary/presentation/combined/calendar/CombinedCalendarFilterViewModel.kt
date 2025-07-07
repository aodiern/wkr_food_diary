package com.example.diary.presentation.combined.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.data.mapper.toUi
import com.example.diary.domain.model.medication.MedicationLog
import com.example.diary.domain.model.symptom.SymptomType
import com.example.diary.domain.model.water.BeverageCategory
import com.example.diary.domain.model.workout.WorkoutType
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
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Locale

@HiltViewModel
class CombinedCalendarFilterViewModel @Inject constructor(
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

    private val _startDate = MutableStateFlow(LocalDate.now().minusWeeks(1))
    val startDate: StateFlow<LocalDate> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow(LocalDate.now())
    val endDate: StateFlow<LocalDate> = _endDate.asStateFlow()


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _entries = MutableStateFlow<List<CombinedEntryDetail>>(emptyList())
    val filteredEntries: StateFlow<List<CombinedEntryDetail>> = _entries.asStateFlow()

    init {
        refreshEntries()
    }

    fun setStartDate(date: LocalDate) {
        _startDate.value = date
        refreshEntries()
    }

    fun setEndDate(date: LocalDate) {
        _endDate.value = date
        refreshEntries()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        refreshEntries()
    }

    private fun refreshEntries() {
        viewModelScope.launch {
            val start = _startDate.value
            val end = _endDate.value
            if (end.isBefore(start)) {
                _entries.value = emptyList()
                return@launch
            }
            val days = ChronoUnit.DAYS.between(start, end).toInt()
            val dates = List(days + 1) { offset -> start.plusDays(offset.toLong()) }

            val combined = mutableListOf<CombinedEntryDetail>()


            getAllMealsUseCase()
                .firstOrNull()
                .orEmpty()
                .map { it.toUi() }
                .filter { it.dateTime.toLocalDate() in start..end }
                .forEach { m ->
                    combined += CombinedEntryDetail.Meal(
                        id = m.mealId,
                        dateTime = m.dateTime,
                        name = m.name,
                        method = m.method,
                        ingredients = m.ingredients
                    )
                }


            val symptomTypes: List<SymptomType> = getAllSymptomTypesUseCase()
                .firstOrNull()
                .orEmpty()
            getAllSymptomsUseCase()
                .firstOrNull()
                .orEmpty()
                .filter { it.dateTime.toLocalDate() in start..end }
                .forEach { s ->
                    val typeName = symptomTypes.firstOrNull { it.typeId == s.typeId }?.name.orEmpty()
                    combined += CombinedEntryDetail.Symptom(
                        id = s.symptomId,
                        dateTime = s.dateTime,
                        typeName = typeName,
                        intensity = s.intensity
                    )
                }


            dates.forEach { date ->
                generateMedicationLogsForDateUseCase(date)
            }

            dates.forEach { date ->
                val logsForDay: List<MedicationLog> =
                    getMedicationLogsForDateUseCase(date)
                        .firstOrNull()
                        .orEmpty()

                Log.d("CombinedVM", "Date $date: ${logsForDay.size} med logs")

                logsForDay
                    .filter { it.taken }
                    .filter { it.scheduledTime.toLocalDate() == date }
                    .forEach { log ->
                        combined += CombinedEntryDetail.Medication(
                            id = log.id,
                            dateTime = log.scheduledTime,
                            name = log.name,
                            dose = log.dose,
                            taken    = log.taken
                        )
                    }
            }


            val bevCats: List<BeverageCategory> = getAllBeverageCategoriesUseCase()
                .firstOrNull()
                .orEmpty()
            dates.forEach { d ->
                getWaterIntakesUseCase(d)
                    .firstOrNull()
                    .orEmpty()
                    .filter { it.timestamp.toLocalDate() == d }
                    .forEach { w ->
                        val categoryName = bevCats.firstOrNull { it.categoryId == w.categoryId }?.name.orEmpty()
                        combined += CombinedEntryDetail.Water(
                            id = w.intakeId,
                            dateTime = w.timestamp,
                            categoryName = categoryName,
                            amountMl = w.amountMl
                        )
                    }
            }


            dates.forEach { d ->
                getSleepSessionsUseCase(d)
                    .firstOrNull()
                    .orEmpty()
                    .filter { it.startTime.toLocalDate() == d }
                    .forEach { s ->
                        combined += CombinedEntryDetail.Sleep(
                            id = s.sessionId,
                            dateTime = s.startTime,
                            start = s.startTime,
                            end = s.endTime,
                            quality = s.quality
                        )
                    }
            }


            val workoutTypes: List<WorkoutType> = getAllWorkoutTypesUseCase()
                .firstOrNull()
                .orEmpty()
            dates.forEach { d ->
                getWorkoutSessionsUseCase(d)
                    .firstOrNull()
                    .orEmpty()
                    .filter { it.startTime.toLocalDate() == d }
                    .forEach { w ->
                        val typeName = workoutTypes.firstOrNull { it.typeId == w.workoutTypeId }?.name.orEmpty()
                        combined += CombinedEntryDetail.Workout(
                            id = w.sessionId,
                            dateTime = w.startTime,
                            typeName = typeName,
                            intensity = w.intensity
                        )
                    }
            }


            dates.forEach { d ->
                stressLogRepository.getLogsForDate(d)
                    .firstOrNull()
                    .orEmpty()
                    .filter { it.timestamp.toLocalDate() == d }
                    .forEach { st ->
                        combined += CombinedEntryDetail.Stress(
                            id = st.logId,
                            dateTime = st.timestamp,
                            level = st.level
                        )
                    }
            }

            val q = _searchQuery.value.trim().lowercase(Locale.getDefault())
            _entries.value = combined
                .filter { it.title.lowercase(Locale.getDefault()).contains(q) }
                .sortedByDescending { it.dateTime }
        }
    }

    sealed class CombinedEntryDetail {
        abstract val id: Long
        abstract val dateTime: LocalDateTime
        abstract val title: String

        data class Meal(
            override val id: Long,
            override val dateTime: LocalDateTime,
            val name: String,
            val method: String,
            val ingredients: List<String>
        ) : CombinedEntryDetail() {
            override val title: String get() = name
        }

        data class Symptom(
            override val id: Long,
            override val dateTime: LocalDateTime,
            val typeName: String,
            val intensity: Int
        ) : CombinedEntryDetail() {
            override val title: String get() = typeName
        }

        data class Medication(
            override val id: Long,
            override val dateTime: LocalDateTime,
            val name: String,
            val dose: String,
            val taken: Boolean
        ) : CombinedEntryDetail() {
            override val title: String get() = "$name ($dose)"
        }

        data class Water(
            override val id: Long,
            override val dateTime: LocalDateTime,
            val categoryName: String,
            val amountMl: Int
        ) : CombinedEntryDetail() {
            override val title: String get() = "$categoryName: $amountMl мл"
        }

        data class Sleep(
            override val id: Long,
            override val dateTime: LocalDateTime,
            val start: LocalDateTime,
            val end: LocalDateTime,
            val quality: Int?
        ) : CombinedEntryDetail() {
            override val title: String get() = "С ${start.toLocalTime()} до ${end.toLocalTime()}"
        }

        data class Workout(
            override val id: Long,
            override val dateTime: LocalDateTime,
            val typeName: String,
            val intensity: Int
        ) : CombinedEntryDetail() {
            override val title: String get() = "$typeName • интенсивность $intensity/10"
        }

        data class Stress(
            override val id: Long,
            override val dateTime: LocalDateTime,
            val level: Int
        ) : CombinedEntryDetail() {
            override val title: String get() = "Стресс $level/10"
        }
    }
}