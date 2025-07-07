package com.example.diary.data.repository

import com.example.diary.data.local.dao.SleepSessionDao
import com.example.diary.data.mapper.toDomain
import com.example.diary.data.mapper.toEntity
import com.example.diary.domain.model.SleepSession
import com.example.diary.domain.repository.SleepSessionRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

@Singleton
class SleepSessionRepositoryImpl @Inject constructor(
    private val dao: SleepSessionDao
) : SleepSessionRepository {
    override fun getSessionsForDate(date: LocalDate): Flow<List<SleepSession>> =
        dao.getSessionsForDate(date)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun addSession(session: SleepSession): Long =
        dao.insert(session.toEntity())

    override suspend fun deleteSession(sessionId: Long) {
        dao.deleteById(sessionId)
    }
    override suspend fun updateSession(session: SleepSession) {
        dao.update(session.toEntity())
    }
}