package com.example.diary.domain.model.stats

import java.time.LocalDate

data class DailyWaterStat(val date: LocalDate, val totalAmountMl: Int, val intakeCount: Int)
data class DailySleepStat(val date: LocalDate, val totalDurationHours: Double, val averageQuality: Double?)
data class MedicationCount(val medicationName: String, val intakeCount: Int)
data class DailyMedicationStat(val date: LocalDate, val counts: List<MedicationCount>)
data class DailyStressStat(val date: LocalDate, val averageLevel: Double, val measurementCount: Int)

data class SymptomFrequency(val name: String, val count: Int)
data class SymptomIntensity(val name: String, val averageIntensity: Double)

data class WorkoutTypeFrequency(val type: String, val count: Int)
data class WorkoutIntensity(val type: String, val maxIntensity: Double)
data class WeeklyWorkoutDuration(val weekStart: LocalDate, val averageDurationMinutes: Double)

data class MealCookingMethod(val method: String, val count: Int)
data class WaterCategoryStat(val category: String, val totalAmountMl: Int)