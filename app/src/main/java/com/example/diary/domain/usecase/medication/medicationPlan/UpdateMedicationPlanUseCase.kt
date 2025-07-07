package com.example.diary.domain.usecase.medication.medicationPlan

import com.example.diary.domain.model.medication.MedicationPlan
import com.example.diary.domain.repository.medication.MedicationPlanRepository

class UpdateMedicationPlanUseCase(
    private val repository: MedicationPlanRepository
) {
    suspend operator fun invoke(plan: MedicationPlan) {
        repository.updatePlan(plan)
    }
}