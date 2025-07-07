package com.example.diary.presentation.notification

data class ScheduledReminder(
    val id: Int,
    val message: String,
    val timeInMillis: Long,
    val repeatInterval: RepeatInterval
)

enum class RepeatInterval { NONE, DAILY, WEEKLY, CUSTOM }
