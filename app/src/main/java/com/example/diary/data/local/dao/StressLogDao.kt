package com.example.diary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.diary.data.local.entity.StressLogEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class StressStatsEntity(
    val date: LocalDate,
    val averageLevel: Double,
    val measurementCount: Int
)
@Dao
interface StressLogDao {
    @Insert
    suspend fun insert(log: StressLogEntity): Long

    @Query("SELECT * FROM stress_logs WHERE date(timestamp) = date(:date) ORDER BY timestamp DESC")
    fun getLogsForDate(date: LocalDate): Flow<List<StressLogEntity>>

    @Query("DELETE FROM stress_logs WHERE logId = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM stress_logs")
    suspend fun getAll(): List<StressLogEntity>

    @Query("""
      SELECT date(timestamp) AS date,
             AVG(level) AS averageLevel,
             COUNT(*) AS measurementCount
      FROM stress_logs
      WHERE date(timestamp) BETWEEN date(:start) AND date(:end)
      GROUP BY date(timestamp)
      ORDER BY date(timestamp)
    """ )
    fun getDailyStats(start: LocalDate, end: LocalDate): Flow<List<StressStatsEntity>>

    @Update
    suspend fun update(log: StressLogEntity)
}