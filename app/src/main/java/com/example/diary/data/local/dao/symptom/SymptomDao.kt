package com.example.diary.data.local.dao.symptom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.diary.data.local.entity.symptom.SymptomEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class SymptomFrequencyEntity(
    val name: String,
    val count: Int
)
data class SymptomIntensityEntity(
    val name: String,
    val avgIntensity: Double
)

@Dao
interface SymptomDao {
    @Query("SELECT * FROM symptoms")
    fun getAll(): Flow<List<SymptomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(symptom: SymptomEntity)

    @Update
    suspend fun update(symptom: SymptomEntity)

    @Delete
    suspend fun delete(symptom: SymptomEntity)

    @Query("""
    SELECT
      typeId AS name,      
      COUNT(*) AS count
    FROM symptoms
    WHERE date(dateTime) BETWEEN date(:start) AND date(:end)
    GROUP BY typeId
    ORDER BY count DESC
    LIMIT :limit
  """)
    fun getMostFrequent(
        start: LocalDate,
        end: LocalDate,
        limit: Int
    ): Flow<List<SymptomFrequencyEntity>>

    @Query("""
    SELECT
      typeId AS name,      
      AVG(intensity) AS avgIntensity
    FROM symptoms
    WHERE date(dateTime) BETWEEN date(:start) AND date(:end)
    GROUP BY typeId
    ORDER BY avgIntensity DESC
    LIMIT :limit
  """)
    fun getHighestIntensity(
        start: LocalDate,
        end: LocalDate,
        limit: Int
    ): Flow<List<SymptomIntensityEntity>>


}