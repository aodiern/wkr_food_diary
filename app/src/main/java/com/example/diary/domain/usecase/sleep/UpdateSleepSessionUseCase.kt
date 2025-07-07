package com.example.diary.domain.usecase.sleep

import com.example.diary.domain.model.SleepSession
import com.example.diary.domain.repository.SleepSessionRepository
import javax.inject.Inject

class UpdateSleepSessionUseCase @Inject constructor(
    private val repository: SleepSessionRepository
) {

    suspend operator fun invoke(session: SleepSession) {
        repository.updateSession(session)
    }
}