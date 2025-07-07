package com.example.diary.presentation.medication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.model.medication.MedicationLog
import com.example.diary.domain.usecase.medication.medicationLog.AddMedicationLogUseCase
import com.example.diary.domain.usecase.medication.medicationLog.DeleteMedicationLogUseCase
import com.example.diary.domain.usecase.medication.medicationLog.GenerateMedicationLogsForDateUseCase
import com.example.diary.domain.usecase.medication.medicationLog.GetMedicationLogsForDateUseCase
import com.example.diary.domain.usecase.medication.medicationLog.UpdateMedicationLogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@HiltViewModel
class MedicationTrackerViewModel @Inject constructor(
    private val getLogs: GetMedicationLogsForDateUseCase,
    private val addLog: AddMedicationLogUseCase,
    private val updateLog: UpdateMedicationLogUseCase,
    private val deleteLog: DeleteMedicationLogUseCase,
    private val generateLogs: GenerateMedicationLogsForDateUseCase
) : ViewModel() {
    private val _logs = MutableStateFlow<List<MedicationLog>>(emptyList())
    val logs: StateFlow<List<MedicationLog>> = _logs


    fun loadLogsFor(date: LocalDate = LocalDate.now()) {
        viewModelScope.launch {

            generateLogs(date)

            getLogs(date).collect { list ->
                _logs.value = list
            }
        }
    }


    fun toggleTaken(log: MedicationLog) {
        viewModelScope.launch {
            updateLog(log.copy(taken = !log.taken))


        }
    }


    fun addAdHocLog(name: String, dose: String, dateTime: LocalDateTime) {
        viewModelScope.launch {
            addLog(
                MedicationLog(
                    id = 0L,
                    planId = null,
                    name = name,
                    dose = dose,
                    scheduledTime = dateTime,
                    taken = false
                )
            )

        }
    }


    fun removeLog(id: Long) {
        viewModelScope.launch {
            deleteLog(id)

        }
    }
}

