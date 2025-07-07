package com.example.diary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.diary.data.local.converter.Converters
import java.time.LocalDateTime

@Entity(tableName = "sleep_sessions")
@TypeConverters(Converters::class)
data class SleepSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Long = 0L,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val quality: Int? = null
)