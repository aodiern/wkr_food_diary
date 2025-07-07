package com.example.diary.domain.repository

import com.example.diary.domain.model.SleepSession
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SleepSessionRepository {
    fun getSessionsForDate(date: LocalDate): Flow<List<SleepSession>>
    suspend fun addSession(session: SleepSession): Long
    suspend fun deleteSession(sessionId: Long)
    suspend fun updateSession(session: SleepSession)
}