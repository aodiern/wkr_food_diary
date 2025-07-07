package com.example.diary.data.local.dao.symptom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.diary.data.local.entity.symptom.SymptomTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SymptomTypeDao {
    @Query("SELECT * FROM symptom_types")
    fun getAll(): Flow<List<SymptomTypeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(type: SymptomTypeEntity)

    @Update
    suspend fun update(type: SymptomTypeEntity)

    @Delete
    suspend fun delete(type: SymptomTypeEntity)
}