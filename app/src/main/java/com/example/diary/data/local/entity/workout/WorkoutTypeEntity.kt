package com.example.diary.data.local.entity.workout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_types")
data class WorkoutTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val typeId: Long = 0L,
    val name: String
)