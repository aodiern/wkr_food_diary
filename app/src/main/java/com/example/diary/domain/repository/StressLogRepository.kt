package com.example.diary.domain.repository

import com.example.diary.domain.model.StressLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StressLogRepository {
    fun getLogsForDate(date: LocalDate): Flow<List<StressLog>>
    suspend fun addLog(log: StressLog): Long
    suspend fun deleteLog(logId: Long)
    suspend fun updateLog(log: StressLog)
}