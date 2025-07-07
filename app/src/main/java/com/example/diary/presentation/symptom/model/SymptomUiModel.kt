package com.example.diary.presentation.symptom.model

import java.time.LocalDateTime

data class SymptomUiModel(
    val symptomId: Long,
    val typeId: Long,
    val typeName: String,
    val intensity: Int,
    val dateTime: LocalDateTime
)