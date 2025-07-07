package com.example.diary.domain.model

import java.time.LocalDateTime

data class StressLog(
    val logId: Long,
    val level: Int,
    val timestamp: LocalDateTime
)