package com.example.diary.domain.repository.workout

import com.example.diary.domain.model.workout.WorkoutSession
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WorkoutSessionRepository {
    fun getSessionsForDate(date: LocalDate): Flow<List<WorkoutSession>>
    suspend fun addSession(session: WorkoutSession): Long
    suspend fun deleteSession(sessionId: Long)
    suspend fun updateSession(session: WorkoutSession)
}