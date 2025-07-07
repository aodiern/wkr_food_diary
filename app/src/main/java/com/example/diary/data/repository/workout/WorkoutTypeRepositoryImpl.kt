package com.example.diary.data.repository.workout

import com.example.diary.data.local.dao.workout.WorkoutTypeDao
import com.example.diary.data.mapper.*
import com.example.diary.domain.model.workout.WorkoutType
import com.example.diary.domain.repository.workout.WorkoutTypeRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class WorkoutTypeRepositoryImpl @Inject constructor(
    private val dao: WorkoutTypeDao
) : WorkoutTypeRepository {
    override fun getAllTypes(): Flow<List<WorkoutType>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun addType(type: WorkoutType): Long =
        dao.insert(type.toEntity())

    override suspend fun deleteType(type: WorkoutType) {
        dao.delete(type.toEntity())
    }
}