package com.example.diary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.diary.data.local.converter.Converters
import java.time.LocalDateTime

@Entity(tableName = "stress_logs")
@TypeConverters(Converters::class)
data class StressLogEntity(
    @PrimaryKey(autoGenerate = true)
    val logId: Long = 0L,
    val level: Int,
    val timestamp: LocalDateTime
)