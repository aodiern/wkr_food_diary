package com.example.diary.domain.usecase.medication.medicationLog

import com.example.diary.domain.repository.medication.MedicationLogRepository

class DeleteMedicationLogUseCase(
    private val repository: MedicationLogRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteLog(id)
    }
}