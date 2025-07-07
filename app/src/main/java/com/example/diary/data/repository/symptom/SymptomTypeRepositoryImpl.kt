package com.example.diary.data.repository.symptom

import com.example.diary.data.local.dao.symptom.SymptomTypeDao
import com.example.diary.data.mapper.toEntity
import com.example.diary.data.mapper.toDomain
import com.example.diary.domain.model.symptom.SymptomType
import com.example.diary.domain.repository.symptom.SymptomTypeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SymptomTypeRepositoryImpl(
    private val dao: SymptomTypeDao
) : SymptomTypeRepository {
    override fun getAllTypes(): Flow<List<SymptomType>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun addType(type: SymptomType) =
        dao.insert(type.toEntity())

    override suspend fun updateType(type: SymptomType) =
        dao.update(type.toEntity())

    override suspend fun deleteType(type: SymptomType) =
        dao.delete(type.toEntity())
}