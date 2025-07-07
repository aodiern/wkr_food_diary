package com.example.diary.data.repository.workout

import com.example.diary.data.local.dao.workout.WorkoutSessionDao
import com.example.diary.data.mapper.*
import com.example.diary.domain.model.workout.WorkoutSession
import com.example.diary.domain.repository.workout.WorkoutSessionRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

@Singleton
class WorkoutSessionRepositoryImpl @Inject constructor(
    private val dao: WorkoutSessionDao
) : WorkoutSessionRepository {
    override fun getSessionsForDate(date: LocalDate): Flow<List<WorkoutSession>> =
        dao.getSessionsForDate(date).map { list -> list.map { it.toDomain() } }

    override suspend fun addSession(session: WorkoutSession): Long =
        dao.insert(session.toEntity())

    override suspend fun deleteSession(sessionId: Long) {
        dao.deleteById(sessionId)
    }
    override suspend fun updateSession(session: WorkoutSession) {
        dao.update(session.toEntity())
    }
}