package com.example.diary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.diary.data.local.entity.SleepSessionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class SleepStatsEntity(
    val date: LocalDate,
    val totalDurationHours: Double,
    val averageQuality: Double?
)

@Dao
interface SleepSessionDao {
    @Insert
    suspend fun insert(session: SleepSessionEntity): Long

    @Query("SELECT * FROM sleep_sessions WHERE date(startTime) = date(:date) ORDER BY startTime DESC")
    fun getSessionsForDate(date: LocalDate): Flow<List<SleepSessionEntity>>

    @Query("DELETE FROM sleep_sessions WHERE sessionId = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM sleep_sessions")
    suspend fun getAll(): List<SleepSessionEntity>

    @Query("""
      SELECT date(startTime) AS date,
             SUM((strftime('%s', endTime) - strftime('%s', startTime)) / 3600.0) AS totalDurationHours,
             AVG(quality) AS averageQuality
      FROM sleep_sessions
      WHERE date(startTime) BETWEEN date(:start) AND date(:end)
      GROUP BY date(startTime)
      ORDER BY date(startTime)
    """ )
    fun getDailyStats(start: LocalDate, end: LocalDate): Flow<List<SleepStatsEntity>>

    @Update
    suspend fun update(session: SleepSessionEntity)
}