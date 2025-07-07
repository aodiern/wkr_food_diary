package com.example.diary.domain.usecase.stress

import com.example.diary.domain.model.StressLog
import com.example.diary.domain.repository.StressLogRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetAllStressLogsUseCase @Inject constructor(
    private val repository: StressLogRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<StressLog>> =
        repository.getLogsForDate(date)
}