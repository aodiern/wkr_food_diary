package com.example.diary.domain.usecase.medication.medicationPlan

import com.example.diary.domain.model.medication.MedicationPlan
import com.example.diary.domain.repository.medication.MedicationPlanRepository
import kotlinx.coroutines.flow.Flow

class GetAllMedicationPlansUseCase(
    private val repository: MedicationPlanRepository
) {
    suspend operator fun invoke(): Flow<List<MedicationPlan>> =
        repository.getAllPlans()
}