package com.example.diary.data.local.dao.medication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.diary.data.local.entity.medication.MedicationLogEntity
import com.example.diary.data.local.entity.medication.MedicationLogWithPlan
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MedicationLogDao {

    @Query("SELECT * FROM medication_logs WHERE date(scheduledTime) = date(:date)")
    fun getForDate(date: LocalDate): Flow<List<MedicationLogEntity>>

    @Query("SELECT * FROM medication_logs WHERE logId = :id")
    suspend fun getById(id: Long): MedicationLogEntity?

    @Insert
    suspend fun insert(log: MedicationLogEntity): Long

    @Update
    suspend fun update(log: MedicationLogEntity)

    @Query("DELETE FROM medication_logs WHERE logId = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM medication_logs")
    suspend fun getAll(): List<MedicationLogEntity>

    @Query("""
    SELECT
      logs.logId            AS logId,
      logs.planId           AS planId,
      logs.scheduledTime    AS scheduledTime,
      logs.taken            AS taken,

      -- если запись ad-hoc (planId IS NULL), берём adHocName / adHocDose,
      -- иначе — имя из справочника через план
      COALESCE(logs.adHocName, defs.name)  AS name,
      COALESCE(logs.adHocDose, plans.dosage) AS dose

    FROM medication_logs AS logs

    LEFT JOIN medication_plans AS plans
      ON logs.planId = plans.planId

    LEFT JOIN medication_defs AS defs
      ON plans.definitionId = defs.defId

    WHERE date(logs.scheduledTime) = date(:date)
    ORDER BY logs.scheduledTime
  """)
    fun getLogsWithPlanDetailsForDate(date: LocalDate): Flow<List<MedicationLogWithPlan>>
}

