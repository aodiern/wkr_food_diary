package com.example.diary.data.local.entity.medication

import java.time.LocalDateTime


data class MedicationLogWithPlan(
    val logId: Long,
    val planId: Long?,
    val scheduledTime: LocalDateTime,
    val taken: Boolean,

    val name: String?,
    val dose: String?
)
