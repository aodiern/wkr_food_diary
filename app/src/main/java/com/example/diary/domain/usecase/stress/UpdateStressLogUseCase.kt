package com.example.diary.domain.usecase.stress

import com.example.diary.domain.model.StressLog
import com.example.diary.domain.repository.StressLogRepository
import javax.inject.Inject

class UpdateStressLogUseCase @Inject constructor(
    private val repository: StressLogRepository
) {
    suspend operator fun invoke(log: StressLog) {
        repository.updateLog(log)
    }
}