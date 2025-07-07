package com.example.diary.domain.usecase.symptom

import com.example.diary.domain.model.symptom.Symptom
import com.example.diary.domain.repository.symptom.SymptomRepository

class DeleteSymptomUseCase(
    private val repository: SymptomRepository
) {
    suspend operator fun invoke(symptom: Symptom) = repository.deleteSymptom(symptom)
}