package com.example.diary.domain.usecase.water

import com.example.diary.domain.model.water.WaterIntake
import com.example.diary.domain.repository.water.WaterIntakeRepository

class AddWaterIntakeUseCase(
    private val repository: WaterIntakeRepository
) {
    suspend operator fun invoke(intake: WaterIntake): Long =
        repository.addIntake(intake)
}