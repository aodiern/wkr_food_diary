package com.example.diary.data.local.entity.symptom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "symptom_types")
data class SymptomTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val typeId: Long = 0,
    val name: String
)