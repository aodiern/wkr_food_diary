package com.example.diary.domain.usecase.medication.medicationDefinition

import com.example.diary.domain.model.medication.MedicationDefinition
import com.example.diary.domain.repository.medication.MedicationDefinitionRepository

class GetMedicationDefinitionUseCase(
    private val repository: MedicationDefinitionRepository
) {
    suspend operator fun invoke(id: Long): MedicationDefinition? =
        repository.getDefinitionById(id)
}