package com.example.diary.data.repository

import com.example.diary.data.local.dao.StressLogDao
import com.example.diary.data.mapper.toDomain
import com.example.diary.data.mapper.toEntity
import com.example.diary.domain.model.StressLog
import com.example.diary.domain.repository.StressLogRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

@Singleton
class StressLogRepositoryImpl @Inject constructor(
    private val dao: StressLogDao
) : StressLogRepository {
    override fun getLogsForDate(date: LocalDate): Flow<List<StressLog>> =
        dao.getLogsForDate(date).map { list -> list.map { it.toDomain() } }

    override suspend fun addLog(log: StressLog): Long =
        dao.insert(log.toEntity())

    override suspend fun deleteLog(logId: Long) {
        dao.deleteById(logId)
    }
    override suspend fun updateLog(log: StressLog) {
        dao.update(log.toEntity())
    }
}