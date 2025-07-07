package com.example.diary.domain.repository.workout

import com.example.diary.domain.model.workout.WorkoutType
import kotlinx.coroutines.flow.Flow

interface WorkoutTypeRepository {
    fun getAllTypes(): Flow<List<WorkoutType>>
    suspend fun addType(type: WorkoutType): Long
    suspend fun deleteType(type: WorkoutType)
}