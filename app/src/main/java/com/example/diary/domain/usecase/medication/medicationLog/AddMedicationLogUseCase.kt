package com.example.diary.domain.usecase.medication.medicationLog

import com.example.diary.domain.model.medication.MedicationLog
import com.example.diary.domain.repository.medication.MedicationLogRepository

class AddMedicationLogUseCase(
    private val repository: MedicationLogRepository
) {
    suspend operator fun invoke(log: MedicationLog): Long =
        repository.addLog(log)
}