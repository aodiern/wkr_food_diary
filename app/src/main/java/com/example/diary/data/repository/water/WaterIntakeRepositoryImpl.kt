package com.example.diary.data.repository.water

import com.example.diary.data.local.dao.water.WaterIntakeDao
import com.example.diary.data.mapper.toDomain
import com.example.diary.data.mapper.toEntity
import com.example.diary.domain.model.water.WaterIntake
import com.example.diary.domain.repository.water.WaterIntakeRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

@Singleton
class WaterIntakeRepositoryImpl @Inject constructor(
    private val dao: WaterIntakeDao
) : WaterIntakeRepository {
    override fun getIntakesForDate(date: LocalDate): Flow<List<WaterIntake>> =
        dao.getIntakesForDate(date).map { list -> list.map { it.toDomain() } }

    override suspend fun addIntake(intake: WaterIntake): Long =
        dao.insert(intake.toEntity())

    override suspend fun deleteIntake(id: Long) {
        dao.deleteById(id)
    }
    override suspend fun updateIntake(intake: WaterIntake) {
        dao.update(intake.toEntity())
    }
}