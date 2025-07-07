package com.example.diary.domain.usecase

import com.example.diary.domain.model.workout.WorkoutSession
import com.example.diary.domain.model.workout.WorkoutType
import com.example.diary.domain.repository.workout.WorkoutSessionRepository
import com.example.diary.domain.repository.workout.WorkoutTypeRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetAllWorkoutTypesUseCase(
    private val repository: WorkoutTypeRepository
) {
    operator fun invoke(): Flow<List<WorkoutType>> = repository.getAllTypes()
}

class AddWorkoutTypeUseCase(
    private val repository: WorkoutTypeRepository
) {
    suspend operator fun invoke(type: WorkoutType): Long = repository.addType(type)
}

class DeleteWorkoutTypeUseCase(
    private val repository: WorkoutTypeRepository
) {
    suspend operator fun invoke(type: WorkoutType) = repository.deleteType(type)
}



class GetWorkoutSessionsForDateUseCase(
    private val repository: WorkoutSessionRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<WorkoutSession>> = repository.getSessionsForDate(date)
}

class AddWorkoutSessionUseCase(
    private val repository: WorkoutSessionRepository
) {
    suspend operator fun invoke(session: WorkoutSession): Long = repository.addSession(session)
}

class DeleteWorkoutSessionUseCase(
    private val repository: WorkoutSessionRepository
) {
    suspend operator fun invoke(sessionId: Long) = repository.deleteSession(sessionId)
}

class UpdateWorkoutSessionUseCase @Inject constructor(
    private val repository: WorkoutSessionRepository
) {
    suspend operator fun invoke(session: WorkoutSession) {
        repository.updateSession(session)
    }
}