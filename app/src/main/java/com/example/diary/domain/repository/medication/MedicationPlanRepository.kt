package com.example.diary.domain.repository.medication

import com.example.diary.domain.model.medication.MedicationPlan
import kotlinx.coroutines.flow.Flow

interface MedicationPlanRepository {
    fun getAllPlans(): Flow<List<MedicationPlan>>
    suspend fun getPlanById(id: Long): MedicationPlan?
    suspend fun addPlan(plan: MedicationPlan): Long
    suspend fun updatePlan(plan: MedicationPlan)
    suspend fun deletePlan(id: Long)
}