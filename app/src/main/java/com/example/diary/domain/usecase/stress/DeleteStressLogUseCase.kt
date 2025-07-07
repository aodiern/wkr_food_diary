package com.example.diary.domain.usecase.stress

import com.example.diary.domain.repository.StressLogRepository
import javax.inject.Inject

class DeleteStressLogUseCase @Inject constructor(
    private val repository: StressLogRepository
) {
    suspend operator fun invoke(id: Long) =
        repository.deleteLog(id)
}