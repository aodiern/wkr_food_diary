package com.example.diary.data.local.entity

import java.time.Duration
import java.time.LocalDate

data class DiaryEntry(
    val date: LocalDate,
    val sleepDuration: Duration,
    val sleepQuality: Int?,
    val stressLevel: Int?,
    val drinks: List<Drink>,
    val workouts: List<Workout>,
    val fastingDuration: Duration,
    val meals: List<Meal>,
    val symptoms: List<Symptom>,
    val medications: List<Medication>
)

data class Drink(val date: LocalDate, val category: String, val volumeMl: Int)
data class Workout(val date: LocalDate, val type: String, val intensity: Double)
data class Meal(val date: LocalDate, val name: String)
data class Symptom(val date: LocalDate, val type: String, val intensity: Int)
data class Medication(val date: LocalDate, val name: String)