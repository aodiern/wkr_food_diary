package com.example.diary.domain.usecase.water

import com.example.diary.domain.model.water.WaterIntake
import com.example.diary.domain.repository.water.WaterIntakeRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetWaterIntakesForDateUseCase(
    private val repository: WaterIntakeRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<WaterIntake>> =
        repository.getIntakesForDate(date)
}