package com.example.diary.data.local.entity.medication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "medication_plans",
    foreignKeys = [ForeignKey(
        entity = MedicationDefinitionEntity::class,
        parentColumns = ["defId"],
        childColumns = ["definitionId"],
        onDelete = CASCADE
    )],
    indices = [Index("definitionId")]
)
data class MedicationPlanEntity(
    @PrimaryKey(autoGenerate = true) val planId: Long = 0L,
    val definitionId: Long,
    val dosage: String,
    val timesPerDay: Int,
    val times: List<LocalTime>,
    val startDate: LocalDate,
    val endDate: LocalDate? = null
)