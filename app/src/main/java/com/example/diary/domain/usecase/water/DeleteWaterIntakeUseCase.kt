package com.example.diary.domain.usecase.water

import com.example.diary.domain.repository.water.WaterIntakeRepository

class DeleteWaterIntakeUseCase(
    private val repository: WaterIntakeRepository
) {
    suspend operator fun invoke(intakeId: Long) =
        repository.deleteIntake(intakeId)
}