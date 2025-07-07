package com.example.diary.data.local.entity.workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.diary.data.local.converter.Converters
import java.time.LocalDateTime

@Entity(
    tableName = "workout_sessions",
    foreignKeys = [ForeignKey(
        entity = WorkoutTypeEntity::class,
        parentColumns = ["typeId"],
        childColumns = ["workoutTypeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(Converters::class)
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Long = 0L,
    val workoutTypeId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val intensity: Int
)