package com.example.diary.domain.model.workout

import java.time.LocalDateTime

data class WorkoutSession(
    val sessionId: Long,
    val workoutTypeId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val intensity: Int
)