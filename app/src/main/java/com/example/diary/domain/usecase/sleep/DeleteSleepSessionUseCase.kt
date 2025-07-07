package com.example.diary.domain.usecase.sleep

import com.example.diary.domain.repository.SleepSessionRepository

class DeleteSleepSessionUseCase(
    private val repository: SleepSessionRepository
) {
    suspend operator fun invoke(sessionId: Long) =
        repository.deleteSession(sessionId)
}
