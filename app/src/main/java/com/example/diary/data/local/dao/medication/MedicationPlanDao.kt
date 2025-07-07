package com.example.diary.data.local.dao.medication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.diary.data.local.entity.medication.MedicationPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationPlanDao {
    @Query("SELECT * FROM medication_plans")
    fun getAll(): Flow<List<MedicationPlanEntity>>

    @Query("SELECT * FROM medication_plans WHERE planId = :id")
    suspend fun getById(id: Long): MedicationPlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plan: MedicationPlanEntity): Long

    @Update
    suspend fun update(plan: MedicationPlanEntity)

    @Query("DELETE FROM medication_plans WHERE planId = :id")
    suspend fun deleteById(id: Long)
}