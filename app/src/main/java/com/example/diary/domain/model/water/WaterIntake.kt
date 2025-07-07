package com.example.diary.domain.model.water

import java.time.LocalDateTime

data class WaterIntake(
    val intakeId: Long,
    val amountMl: Int,
    val categoryId: Long,
    val timestamp: LocalDateTime
)