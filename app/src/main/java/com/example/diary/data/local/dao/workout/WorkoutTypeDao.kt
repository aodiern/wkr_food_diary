package com.example.diary.data.local.dao.workout

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.diary.data.local.entity.workout.WorkoutTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutTypeDao {
    @Insert
    suspend fun insert(type: WorkoutTypeEntity): Long

    @Query("SELECT * FROM workout_types ORDER BY name")
    fun getAll(): Flow<List<WorkoutTypeEntity>>

    @Delete
    suspend fun delete(type: WorkoutTypeEntity)
}