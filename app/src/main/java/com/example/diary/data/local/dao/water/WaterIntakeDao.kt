package com.example.diary.data.local.dao.water

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.diary.data.local.entity.water.WaterCategoryStatsEntity
import com.example.diary.data.local.entity.water.WaterIntakeEntity
import com.example.diary.data.local.entity.water.WaterIntakeStatsEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class WaterIntakeStatsEntity(val date: LocalDate, val totalAmountMl: Int, val intakeCount: Int)
data class WaterCategoryStatsEntity(val category: String, val totalAmountMl: Int)
@Dao
interface WaterIntakeDao {
    @Insert
    suspend fun insert(intake: WaterIntakeEntity): Long

    @Query("SELECT * FROM water_intake WHERE date(timestamp) = date(:date) ORDER BY timestamp DESC")
    fun getIntakesForDate(date: LocalDate): Flow<List<WaterIntakeEntity>>

    @Query("DELETE FROM water_intake WHERE intakeId = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM water_intake")
    suspend fun getAll(): List<WaterIntakeEntity>

    @Query("""
      SELECT date(timestamp) AS date, SUM(amountMl) AS totalAmountMl, COUNT(*) AS intakeCount
      FROM water_intake
      WHERE date(timestamp) BETWEEN date(:start) AND date(:end)
      GROUP BY date(timestamp)
      ORDER BY date(timestamp)
    """ )
    fun getDailyStats(start: LocalDate, end: LocalDate): Flow<List<WaterIntakeStatsEntity>>

    @Query("""
      SELECT 
        categoryId AS category,  
        SUM(amountMl) AS totalAmountMl
      FROM water_intake
      WHERE date(timestamp) BETWEEN date(:start) AND date(:end)
      GROUP BY categoryId
      ORDER BY totalAmountMl DESC
    """)
    fun getCategoryStats(start: LocalDate, end: LocalDate): Flow<List<WaterCategoryStatsEntity>>

    @Update
    suspend fun update(entity: WaterIntakeEntity)

}