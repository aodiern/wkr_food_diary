package com.example.diary.data.local.entity.symptom

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "symptoms")
data class SymptomEntity(
    @PrimaryKey(autoGenerate = true)
    val symptomId: Long = 0,
    val typeId: Long,
    val intensity: Int,
    val dateTime: LocalDateTime
)