package com.example.diary.domain.usecase.medication.medicationDefinition

import com.example.diary.domain.model.medication.MedicationDefinition
import com.example.diary.domain.repository.medication.MedicationDefinitionRepository

class AddMedicationDefinitionUseCase(
    private val repository: MedicationDefinitionRepository
) {
    suspend operator fun invoke(def: MedicationDefinition): Long =
        repository.addDefinition(def)
}