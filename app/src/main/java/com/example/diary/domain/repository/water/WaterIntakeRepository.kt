package com.example.diary.domain.repository.water

import com.example.diary.domain.model.water.WaterIntake
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WaterIntakeRepository {
    fun getIntakesForDate(date: LocalDate): Flow<List<WaterIntake>>
    suspend fun addIntake(intake: WaterIntake): Long
    suspend fun deleteIntake(id: Long)
    suspend fun updateIntake(intake: WaterIntake)
}