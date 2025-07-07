package com.example.diary.data.repository.medication

import com.example.diary.data.local.dao.medication.MedicationPlanDao
import com.example.diary.data.mapper.toDomain
import com.example.diary.data.mapper.toEntity
import com.example.diary.domain.model.medication.MedicationPlan
import com.example.diary.domain.repository.medication.MedicationPlanRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class MedicationPlanRepositoryImpl @Inject constructor(
    private val dao: MedicationPlanDao
) : MedicationPlanRepository {
    override fun getAllPlans(): Flow<List<MedicationPlan>> =
        dao.getAll()
            .map { entityList -> entityList.map { it.toDomain() } }

    override suspend fun getPlanById(id: Long): MedicationPlan? =
        dao.getById(id)?.toDomain()

    override suspend fun addPlan(plan: MedicationPlan): Long =
        dao.insert(plan.toEntity())

    override suspend fun updatePlan(plan: MedicationPlan) {
        dao.update(plan.toEntity())
    }

    override suspend fun deletePlan(id: Long) {
        dao.deleteById(id)
    }
}
