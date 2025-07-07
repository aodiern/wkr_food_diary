package com.example.diary.domain.repository.symptom

import com.example.diary.domain.model.symptom.Symptom
import kotlinx.coroutines.flow.Flow

interface SymptomRepository {
    fun getAllSymptoms(): Flow<List<Symptom>>
    suspend fun addSymptom(symptom: Symptom)
    suspend fun updateSymptom(symptom: Symptom)
    suspend fun deleteSymptom(symptom: Symptom)
}