package com.example.diary.domain.usecase.symptom

import com.example.diary.domain.model.symptom.SymptomType
import com.example.diary.domain.repository.symptom.SymptomTypeRepository

class DeleteSymptomTypeUseCase(
    private val repository: SymptomTypeRepository
) {
    suspend operator fun invoke(type: SymptomType) = repository.deleteType(type)
}