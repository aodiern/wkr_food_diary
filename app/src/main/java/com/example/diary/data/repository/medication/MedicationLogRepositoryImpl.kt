package com.example.diary.data.repository.medication

import com.example.diary.data.local.dao.medication.MedicationDefinitionDao
import com.example.diary.data.local.dao.medication.MedicationLogDao
import com.example.diary.data.local.dao.medication.MedicationPlanDao
import com.example.diary.data.local.entity.medication.MedicationLogEntity
import com.example.diary.data.local.entity.medication.MedicationLogWithPlan
import com.example.diary.data.mapper.toEntity
import com.example.diary.domain.model.medication.MedicationLog
import com.example.diary.domain.repository.medication.MedicationLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationLogRepositoryImpl @Inject constructor(
    private val logDao: MedicationLogDao,
    private val planDao: MedicationPlanDao,
    private val definitionDao: MedicationDefinitionDao
) : MedicationLogRepository {

    override fun getLogsForDate(date: LocalDate): Flow<List<MedicationLog>> =
        logDao.getForDate(date)
            .map { entities ->
                entities.map { entity ->
                    
                    if (entity.planId != null) {
                        val plan = planDao.getById(entity.planId)
                        val def = plan?.definitionId?.let { definitionDao.getById(it) }
                        MedicationLog(
                            id = entity.logId,
                            planId = entity.planId,
                            scheduledTime = entity.scheduledTime,
                            taken = entity.taken,
                            name = def?.name.orEmpty(),
                            dose = plan?.dosage.orEmpty()
                        )
                    } else {
                        
                        MedicationLog(
                            id = entity.logId,
                            planId = null,
                            scheduledTime = entity.scheduledTime,
                            taken = entity.taken,
                            name = entity.adHocName.orEmpty(),
                            dose = entity.adHocDose.orEmpty()
                        )
                    }
                }
            }

    override suspend fun addLog(log: MedicationLog): Long {
        val entity = log.toEntity()
        return logDao.insert(entity)
    }

    override suspend fun updateLog(log: MedicationLog) {
        val entity = log.toEntity()
        logDao.update(entity)
    }

    override suspend fun deleteLog(id: Long) {
        logDao.deleteById(id)
    }
}
