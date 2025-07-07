package com.example.diary.data.repository.medication

import com.example.diary.data.local.dao.medication.MedicationDefinitionDao
import com.example.diary.data.mapper.toDomain
import com.example.diary.data.mapper.toEntity
import com.example.diary.domain.model.medication.MedicationDefinition
import com.example.diary.domain.repository.medication.MedicationDefinitionRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class MedicationDefinitionRepositoryImpl @Inject constructor(
    private val dao: MedicationDefinitionDao
) : MedicationDefinitionRepository {
    override suspend fun getAllDefinitions(): List<MedicationDefinition> =
        dao.getAll().map { it.toDomain() }

    override suspend fun getDefinitionById(id: Long): MedicationDefinition? =
        dao.getById(id)?.toDomain()

    override suspend fun addDefinition(def: MedicationDefinition): Long =
        dao.insert(def.toEntity())

    override suspend fun updateDefinition(def: MedicationDefinition) {
        dao.update(def.toEntity())
    }

    override suspend fun deleteDefinition(id: Long) {
        dao.deleteById(id)
    }
}
