package com.example.diary.domain.model.stats

import com.example.diary.data.local.entity.DiaryEntry
import java.time.LocalDate

object StatsCalculations {

    fun totalSleepHours(entries: List<DiaryEntry>, weekStart: LocalDate): Double {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .filter { it.date in weekStart..weekEnd }
            .sumOf { it.sleepDuration.toMinutes().toDouble() } / 60.0
    }

    fun drinksByCategory(entries: List<DiaryEntry>, weekStart: LocalDate): Map<String, Int> {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .flatMap { it.drinks }
            .filter { it.date in weekStart..weekEnd }
            .groupingBy { it.category }
            .eachCount()
    }

    fun fluidByDay(entries: List<DiaryEntry>, weekStart: LocalDate): Map<LocalDate, Int> {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .filter { it.date in weekStart..weekEnd }
            .groupBy { it.date }
            .mapValues { (_, dayEntries) ->
                dayEntries.sumOf { entry -> entry.drinks.sumOf { it.volumeMl } }
            }
    }


    fun averageSleepQuality(entries: List<DiaryEntry>, weekStart: LocalDate): Double {
        val weekEnd = weekStart.plusDays(6)
        val qualities = entries
            .filter { it.date in weekStart..weekEnd }
            .mapNotNull { it.sleepQuality }
        return if (qualities.isNotEmpty()) qualities.average() else 0.0
    }


    fun maxStressByDay(entries: List<DiaryEntry>, weekStart: LocalDate): Map<LocalDate, Int> {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .filter { it.date in weekStart..weekEnd }
            .groupBy { it.date }
            .mapValues { (_, dayEntries) ->
                dayEntries.maxOfOrNull { it.stressLevel ?: 0 } ?: 0
            }
    }


    fun workoutsByCategory(entries: List<DiaryEntry>, weekStart: LocalDate): Map<String, Int> {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .flatMap { it.workouts }
            .filter { it.date in weekStart..weekEnd }
            .groupingBy { it.type }
            .eachCount()
    }


    fun avgWorkoutIntensity(entries: List<DiaryEntry>, weekStart: LocalDate): Map<String, Double> {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .flatMap { it.workouts }
            .filter { it.date in weekStart..weekEnd }
            .groupBy { it.type }
            .mapValues { (_, list) -> list.map { it.intensity }.average() }
    }

    fun mainWorkoutFrequency(entries: List<DiaryEntry>, weekStart: LocalDate): Map<String, Int> {
        return workoutsByCategory(entries, weekStart)
    }

    fun longestFastingByDay(entries: List<DiaryEntry>, weekStart: LocalDate): Map<LocalDate, Double> {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .filter { it.date in weekStart..weekEnd }
            .groupBy { it.date }
            .mapValues { (_, dayEntries) ->
                dayEntries.maxOfOrNull { it.fastingDuration.toHours().toDouble() } ?: 0.0
            }
    }

    fun mealsPerDay(entries: List<DiaryEntry>, weekStart: LocalDate): Map<LocalDate, Int> {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .filter { it.date in weekStart..weekEnd }
            .groupBy { it.date }
            .mapValues { (_, dayEntries) -> dayEntries.sumOf { it.meals.size } }
    }


    fun symptomIntensityByType(entries: List<DiaryEntry>, weekStart: LocalDate): Map<String, Double> {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .flatMap { it.symptoms }
            .filter { it.date in weekStart..weekEnd }
            .groupBy { it.type }
            .mapValues { (_, list) -> list.map { it.intensity }.average() }
    }


    fun symptomsByDay(entries: List<DiaryEntry>, weekStart: LocalDate): Map<LocalDate, List<String>> {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .filter { it.date in weekStart..weekEnd }
            .groupBy { it.date }
            .mapValues { (_, dayEntries) -> dayEntries.flatMap { it.symptoms }.map { it.type } }
    }


    fun medicationsTaken(entries: List<DiaryEntry>, weekStart: LocalDate): Map<LocalDate, List<String>> {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .filter { it.date in weekStart..weekEnd }
            .groupBy { it.date }
            .mapValues { (_, dayEntries) -> dayEntries.flatMap { it.medications }.map { it.name } }
    }

    fun sleepByDay(entries: List<DiaryEntry>, weekStart: LocalDate): Map<LocalDate, Double> {
        val weekEnd = weekStart.plusDays(6)
        return entries
            .filter { it.date in weekStart..weekEnd }
            .groupBy { it.date }
            .mapValues { (_, day) ->
                day.sumOf { it.sleepDuration.toMinutes().toDouble() } / 60.0
            }
    }

}
