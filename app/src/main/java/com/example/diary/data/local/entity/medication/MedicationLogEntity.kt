package com.example.diary.data.local.entity.medication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "medication_logs",
    foreignKeys = [ForeignKey(
        entity = MedicationPlanEntity::class,
        parentColumns = ["planId"],
        childColumns = ["planId"],
        onDelete = CASCADE
    )],
    indices = [Index("planId")]
)
data class MedicationLogEntity(
    @PrimaryKey(autoGenerate = true) val logId: Long = 0L,
    val planId: Long? = null,
    val adHocName: String? = null,
    val adHocDose: String? = null,
    val scheduledTime: LocalDateTime,
    val taken: Boolean = false
)