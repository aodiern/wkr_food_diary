package com.example.diary.domain.model.medication

import java.time.LocalDate
import java.time.LocalTime

data class MedicationPlan(
    val id: Long,
    val definitionId: Long,
    val dosage: String,
    val timesPerDay: Int,
    val times: List<LocalTime>,
    val startDate: LocalDate,
    val endDate: LocalDate? = null
)