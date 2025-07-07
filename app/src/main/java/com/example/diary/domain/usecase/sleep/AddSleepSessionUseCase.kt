package com.example.diary.domain.usecase.sleep

import com.example.diary.domain.model.SleepSession
import com.example.diary.domain.repository.SleepSessionRepository

class AddSleepSessionUseCase(
    private val repository: SleepSessionRepository
) {
    suspend operator fun invoke(session: SleepSession): Long =
        repository.addSession(session)
}