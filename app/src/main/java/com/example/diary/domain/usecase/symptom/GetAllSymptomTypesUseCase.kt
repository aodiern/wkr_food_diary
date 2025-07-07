package com.example.diary.domain.usecase.symptom

import com.example.diary.domain.model.symptom.SymptomType
import com.example.diary.domain.repository.symptom.SymptomTypeRepository
import kotlinx.coroutines.flow.Flow

class GetAllSymptomTypesUseCase(
    private val repository: SymptomTypeRepository
) {
    operator fun invoke(): Flow<List<SymptomType>> = repository.getAllTypes()
}