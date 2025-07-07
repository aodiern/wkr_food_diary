package com.example.diary.domain.model

import java.time.LocalDateTime

data class SleepSession(
    val sessionId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val quality: Int? = null
)