package com.example.diary.domain.repository.medication

import com.example.diary.domain.model.medication.MedicationDefinition

interface MedicationDefinitionRepository {
    suspend fun getAllDefinitions(): List<MedicationDefinition>
    suspend fun getDefinitionById(id: Long): MedicationDefinition?
    suspend fun addDefinition(def: MedicationDefinition): Long
    suspend fun updateDefinition(def: MedicationDefinition)
    suspend fun deleteDefinition(id: Long)
}