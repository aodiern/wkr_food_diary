package com.example.diary.domain.usecase.sleep

import com.example.diary.domain.model.SleepSession
import com.example.diary.domain.repository.SleepSessionRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetSleepSessionsForDateUseCase(
    private val repository: SleepSessionRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<SleepSession>> =
        repository.getSessionsForDate(date)
}