package com.example.diary.domain.usecase.water

import com.example.diary.domain.model.water.WaterIntake
import com.example.diary.domain.repository.water.WaterIntakeRepository
import javax.inject.Inject

class UpdateWaterIntakeUseCase @Inject constructor(
    private val repository: WaterIntakeRepository
) {
    suspend operator fun invoke(waterIntake: WaterIntake) {
        repository.updateIntake(waterIntake)
    }
}