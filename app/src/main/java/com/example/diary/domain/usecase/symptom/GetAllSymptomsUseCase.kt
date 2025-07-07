package com.example.diary.domain.usecase.symptom

import com.example.diary.domain.model.symptom.Symptom
import com.example.diary.domain.repository.symptom.SymptomRepository
import kotlinx.coroutines.flow.Flow

class GetAllSymptomsUseCase(
    private val repository: SymptomRepository
) {
    operator fun invoke(): Flow<List<Symptom>> = repository.getAllSymptoms()
}