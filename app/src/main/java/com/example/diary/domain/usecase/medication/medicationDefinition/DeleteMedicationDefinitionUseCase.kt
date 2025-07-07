package com.example.diary.domain.usecase.medication.medicationDefinition

import com.example.diary.domain.repository.medication.MedicationDefinitionRepository

class DeleteMedicationDefinitionUseCase(
    private val repository: MedicationDefinitionRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteDefinition(id)
    }
}