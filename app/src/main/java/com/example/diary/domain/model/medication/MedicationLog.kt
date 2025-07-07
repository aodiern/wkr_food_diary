package com.example.diary.domain.model.medication

import java.time.LocalDateTime

data class MedicationLog(
    val id: Long,
    val planId: Long?,
    val name: String,
    val dose: String,
    val scheduledTime: LocalDateTime,
    val taken: Boolean
)