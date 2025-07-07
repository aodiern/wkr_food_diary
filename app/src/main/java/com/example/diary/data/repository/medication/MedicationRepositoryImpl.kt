package com.example.diary.data.repository.medication

import com.example.diary.data.local.dao.medication.MedicationDefinitionDao
import com.example.diary.data.local.dao.medication.MedicationLogDao
import com.example.diary.data.local.dao.medication.MedicationPlanDao
import com.example.diary.data.local.entity.medication.MedicationDefinitionEntity
import com.example.diary.data.local.entity.medication.MedicationLogEntity
import com.example.diary.data.local.entity.medication.MedicationPlanEntity
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class MedicationRepositoryImpl @Inject constructor(
    private val defDao: MedicationDefinitionDao,
    private val planDao: MedicationPlanDao,
    private val logDao: MedicationLogDao
) {
    suspend fun getAllDefs() = defDao.getAll()
    suspend fun addDef(d: MedicationDefinitionEntity) = defDao.insert(d)
    

    suspend fun getPlans() = planDao.getAll()
    suspend fun addPlan(p: MedicationPlanEntity) = planDao.insert(p)
    

    suspend fun getLogsFor(date: LocalDate) = logDao.getForDate(date)
    suspend fun addLog(log: MedicationLogEntity) = logDao.insert(log)
    suspend fun updateLog(log: MedicationLogEntity) = logDao.update(log)

    
    suspend fun generateLogsForDay(day: LocalDate) {
        
        val existingLogs: List<MedicationLogEntity> =
            logDao.getForDate(day)
                .firstOrNull()   
                ?: emptyList()   

        
        val plans: List<MedicationPlanEntity> =
            planDao.getAll()
                .firstOrNull()
                ?: emptyList()

        for (plan in plans) {
            for (time in plan.times) {
                val dt = LocalDateTime.of(day, time)
                
                if (existingLogs.none { it.planId == plan.planId && it.scheduledTime == dt }) {
                    logDao.insert(
                        MedicationLogEntity(
                            planId = plan.planId,
                            scheduledTime = dt
                        )
                    )
                }
            }
        }
    }
}