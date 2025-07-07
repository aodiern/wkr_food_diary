package com.example.diary.presentation.combined.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.DiaryEntry
import com.example.diary.domain.model.MealDetails
import com.example.diary.domain.model.SleepSession
import com.example.diary.domain.model.StressLog
import com.example.diary.domain.model.medication.MedicationLog
import com.example.diary.domain.model.water.WaterIntake
import com.example.diary.domain.model.symptom.Symptom
import com.example.diary.domain.model.workout.WorkoutSession
import com.example.diary.domain.usecase.DeleteWorkoutSessionUseCase
import com.example.diary.domain.usecase.GetAllWorkoutTypesUseCase
import com.example.diary.domain.usecase.GetWorkoutSessionsForDateUseCase
import com.example.diary.domain.usecase.UpdateWorkoutSessionUseCase
import com.example.diary.domain.usecase.meal.DeleteMealUseCase
import com.example.diary.domain.usecase.meal.GetAllMealsWithDetailsUseCase
import com.example.diary.domain.usecase.meal.UpdateMealUseCase
import com.example.diary.domain.usecase.medication.medicationLog.DeleteMedicationLogUseCase
import com.example.diary.domain.usecase.medication.medicationLog.GetMedicationLogsForDateUseCase
import com.example.diary.domain.usecase.medication.medicationLog.UpdateMedicationLogUseCase
import com.example.diary.domain.usecase.sleep.DeleteSleepSessionUseCase
import com.example.diary.domain.usecase.sleep.GetSleepSessionsForDateUseCase
import com.example.diary.domain.usecase.sleep.UpdateSleepSessionUseCase
import com.example.diary.domain.usecase.stress.DeleteStressLogUseCase
import com.example.diary.domain.usecase.stress.GetAllStressLogsUseCase
import com.example.diary.domain.usecase.stress.UpdateStressLogUseCase
import com.example.diary.domain.usecase.symptom.DeleteSymptomUseCase
import com.example.diary.domain.usecase.symptom.GetAllSymptomTypesUseCase
import com.example.diary.domain.usecase.symptom.GetAllSymptomsUseCase
import com.example.diary.domain.usecase.symptom.UpdateSymptomUseCase
import com.example.diary.domain.usecase.water.DeleteWaterIntakeUseCase
import com.example.diary.domain.usecase.water.GetAllBeverageCategoriesUseCase
import com.example.diary.domain.usecase.water.GetWaterIntakesForDateUseCase
import com.example.diary.domain.usecase.water.UpdateWaterIntakeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

data class CombinedListUiState(
    val date: LocalDate = LocalDate.now(),
    val entries: List<DiaryEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class EntryType {
    MEAL, WATER, SLEEP, STRESS, SYMPTOM,
    WORKOUT, MEDICATION
}


@HiltViewModel
class CombinedListViewModel @Inject constructor(
    private val getAllMeals: GetAllMealsWithDetailsUseCase,
    private val updateMealUseCase: UpdateMealUseCase,
    private val deleteMeal: DeleteMealUseCase,

    private val getWater: GetWaterIntakesForDateUseCase,
    private val updateWaterUseCase: UpdateWaterIntakeUseCase,
    private val deleteWater: DeleteWaterIntakeUseCase,
    private val getBeverageCategories: GetAllBeverageCategoriesUseCase,

    private val getSleep: GetSleepSessionsForDateUseCase,
    private val updateSleepUseCase: UpdateSleepSessionUseCase,
    private val deleteSleep: DeleteSleepSessionUseCase,

    private val getStress: GetAllStressLogsUseCase,
    private val updateStressUseCase: UpdateStressLogUseCase,
    private val deleteStress: DeleteStressLogUseCase,

    private val getSymptomTypes: GetAllSymptomTypesUseCase,
    private val getSymptoms: GetAllSymptomsUseCase,
    private val updateSymptomUseCase: UpdateSymptomUseCase,
    private val deleteSymptom: DeleteSymptomUseCase,

    private val getWorkouts: GetWorkoutSessionsForDateUseCase,
    private val updateWorkoutUseCase: UpdateWorkoutSessionUseCase,
    private val deleteWorkout: DeleteWorkoutSessionUseCase,
    private val getWorkoutTypes: GetAllWorkoutTypesUseCase,

    private val getMedLogs: GetMedicationLogsForDateUseCase,
    private val updateMedicationUseCase: UpdateMedicationLogUseCase,
    private val deleteMedLog: DeleteMedicationLogUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CombinedListUiState())
    val uiState: StateFlow<CombinedListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        loadEntries(LocalDate.now())
    }

    fun onEvent(event: CombinedListEvent) {
        when (event) {
            is CombinedListEvent.LoadForDate -> loadEntries(event.date)
            is CombinedListEvent.DeleteEntry -> deleteEntry(event.entry)
            is CombinedListEvent.EditEntry -> navigateToEdit(event.entry)
        }
    }

    private fun deleteEntry(entry: DiaryEntry) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        when (entry) {
            is DiaryEntry.MealItem       -> deleteMeal(entry.mealWithDetails.meal)
            is DiaryEntry.WaterItem      -> deleteWater(entry.id)
            is DiaryEntry.SleepItem      -> deleteSleep(entry.id)
            is DiaryEntry.StressItem     -> deleteStress(entry.id)
            is DiaryEntry.SymptomItem    -> deleteSymptom(entry.symptom)
            is DiaryEntry.WorkoutItem    -> deleteWorkout(entry.id)
            is DiaryEntry.MedicationItem -> deleteMedLog(entry.id)
        }
        loadEntries(_uiState.value.date)
    }

    private fun navigateToEdit(entry: DiaryEntry) = viewModelScope.launch {
        _events.emit(UiEvent.NavigateToEdit(entry))
    }

    private fun loadEntries(date: LocalDate) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, date = date, error = null) }


        val symptomTypeMap: Map<Long, String> = getSymptomTypes()
            .first()
            .associate { it.typeId to it.name }

        val workoutTypeMap: Map<Long, String> = getWorkoutTypes()
            .first()
            .associate { it.typeId to it.name }

        val beverageMap: Map<Long, String> = getBeverageCategories()
            .first()
            .associate { it.categoryId to it.name }




        val mealsFlow = getAllMeals()
            .map { list ->
                list.filter { it.meal.dateTime.toLocalDate() == date }
                    .map { DiaryEntry.MealItem(it) }
            }

        val waterFlow = getWater(date)
            .map { list ->
                list.map { wi ->

                    DiaryEntry.WaterItem(
                        waterIntake = wi,
                        categoryName = beverageMap[wi.categoryId] ?: "Unknown"
                    )
                }
            }

        val sleepFlow = getSleep(date)
            .map { list -> list.map { DiaryEntry.SleepItem(it) } }

        val stressFlow = getStress(date)
            .map { list -> list.map { DiaryEntry.StressItem(it) } }

        val symptomFlow = getSymptoms()
            .map { list ->
                list.filter { it.dateTime.toLocalDate() == date }
                    .map { s ->
                        DiaryEntry.SymptomItem(
                            symptom = s,
                            typeName = symptomTypeMap[s.typeId] ?: "Unknown"
                        )
                    }
            }

        val workoutFlow = getWorkouts(date)
            .map { list ->
                list.map { ws ->
                    DiaryEntry.WorkoutItem(
                        session = ws,
                        workoutTypeName = workoutTypeMap[ws.workoutTypeId] ?: "Unknown"
                    )
                }
            }


        val medFlow = getMedLogs(date)
            .map { list -> list.map { DiaryEntry.MedicationItem(it) } }


        val allFlow: Flow<List<DiaryEntry>> = combine(
            listOf(
                mealsFlow as Flow<List<DiaryEntry>>,
                waterFlow,
                sleepFlow,
                stressFlow,
                symptomFlow,
                workoutFlow,
                medFlow
            )
        ) { arrays ->
            arrays.toList()
                .flatten()
                .sortedBy { it.time }
        }

        allFlow
            .catch { e -> _uiState.update { it.copy(error = e.message ?: "Error") } }
            .collect { entries ->
                _uiState.update { it.copy(entries = entries, isLoading = false) }
            }
    }

    /** Inline update methods for dialogs **/
    fun updateMeal(details: MealDetails) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        updateMealUseCase(details.meal)
        loadEntries(_uiState.value.date)
    }

    fun updateWater(water: WaterIntake) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        updateWaterUseCase(water)
        loadEntries(_uiState.value.date)
    }

    fun updateSleep(session: SleepSession) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        updateSleepUseCase(session)
        loadEntries(_uiState.value.date)
    }

    fun updateStress(log: StressLog) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        updateStressUseCase(log)
        loadEntries(_uiState.value.date)
    }

    fun updateSymptom(symptom: Symptom) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        updateSymptomUseCase(symptom)
        loadEntries(_uiState.value.date)
    }

    fun updateWorkout(session: WorkoutSession) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        updateWorkoutUseCase(session)
        loadEntries(_uiState.value.date)
    }

    fun updateMedication(log: MedicationLog) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        updateMedicationUseCase(log)
        loadEntries(_uiState.value.date)
    }

    sealed class CombinedListEvent {
        data class LoadForDate(val date: LocalDate) : CombinedListEvent()
        data class DeleteEntry(val entry: DiaryEntry) : CombinedListEvent()
        data class EditEntry(val entry: DiaryEntry) : CombinedListEvent()
    }

    sealed class UiEvent {
        data class NavigateToEdit(val entry: DiaryEntry) : UiEvent()
    }
}
