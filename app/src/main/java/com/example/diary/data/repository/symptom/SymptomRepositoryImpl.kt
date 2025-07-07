package com.example.diary.data.repository.symptom

import com.example.diary.data.local.dao.symptom.SymptomDao
import com.example.diary.data.mapper.toDomain
import com.example.diary.data.mapper.toEntity
import com.example.diary.domain.model.symptom.Symptom
import com.example.diary.domain.repository.symptom.SymptomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SymptomRepositoryImpl(
    private val dao: SymptomDao
) : SymptomRepository {
    override fun getAllSymptoms(): Flow<List<Symptom>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun addSymptom(symptom: Symptom) =
        dao.insert(symptom.toEntity())

    override suspend fun updateSymptom(symptom: Symptom) =
        dao.update(symptom.toEntity())

    override suspend fun deleteSymptom(symptom: Symptom) =
        dao.delete(symptom.toEntity())
}