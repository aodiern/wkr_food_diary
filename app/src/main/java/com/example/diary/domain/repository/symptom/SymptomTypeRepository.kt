package com.example.diary.domain.repository.symptom

import com.example.diary.domain.model.symptom.SymptomType
import kotlinx.coroutines.flow.Flow

interface SymptomTypeRepository {
    fun getAllTypes(): Flow<List<SymptomType>>
    suspend fun addType(type: SymptomType)
    suspend fun updateType(type: SymptomType)
    suspend fun deleteType(type: SymptomType)
}