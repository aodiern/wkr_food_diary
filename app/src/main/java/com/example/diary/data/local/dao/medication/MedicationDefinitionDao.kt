package com.example.diary.data.local.dao.medication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.diary.data.local.entity.medication.MedicationDefinitionEntity

@Dao
interface MedicationDefinitionDao {
    @Query("SELECT * FROM medication_defs")
    suspend fun getAll(): List<MedicationDefinitionEntity>

    @Query("SELECT * FROM medication_defs WHERE defId = :id")
    suspend fun getById(id: Long): MedicationDefinitionEntity?

    @Insert
    suspend fun insert(def: MedicationDefinitionEntity): Long

    @Update
    suspend fun update(def: MedicationDefinitionEntity)

    @Query("DELETE FROM medication_defs WHERE defId = :id")
    suspend fun deleteById(id: Long)
}