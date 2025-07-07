package com.example.diary.data.local.entity.medication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_defs")
data class MedicationDefinitionEntity(
    @PrimaryKey(autoGenerate = true) val defId: Long = 0L,
    val name: String,
    val form: String? = null,
    val strength: String? = null
)