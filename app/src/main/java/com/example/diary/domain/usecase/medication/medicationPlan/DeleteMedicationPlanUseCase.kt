package com.example.diary.domain.usecase.medication.medicationPlan

import com.example.diary.domain.repository.medication.MedicationPlanRepository

class DeleteMedicationPlanUseCase(
    private val repository: MedicationPlanRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deletePlan(id)
    }
}