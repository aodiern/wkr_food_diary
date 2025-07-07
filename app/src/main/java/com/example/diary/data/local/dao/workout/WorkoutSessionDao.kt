package com.example.diary.data.local.dao.workout

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.diary.data.local.entity.workout.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WorkoutSessionDao {
    /** Вставка новой сессии тренировки */
    @Insert
    suspend fun insert(session: WorkoutSessionEntity): Long

    /** Получение всех сессий за указанную дату по дате начала */
    @Query("SELECT * FROM workout_sessions WHERE date(startTime) = date(:date) ORDER BY startTime DESC")
    fun getSessionsForDate(date: LocalDate): Flow<List<WorkoutSessionEntity>>

    /** Удаление сессии по ID */
    @Query("DELETE FROM workout_sessions WHERE sessionId = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM workout_sessions")
    suspend fun getAll(): List<WorkoutSessionEntity>

    @Update
    suspend fun update(session: WorkoutSessionEntity)
}