package com.example.diary.domain.model.symptom

import java.time.LocalDateTime

data class Symptom(
    val symptomId: Long,
    val typeId: Long,
    val intensity: Int,
    val dateTime: LocalDateTime
)