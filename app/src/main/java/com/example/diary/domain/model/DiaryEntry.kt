package com.example.diary.domain.model

import com.example.diary.domain.model.medication.MedicationLog
import com.example.diary.domain.model.symptom.Symptom
import com.example.diary.domain.model.water.WaterIntake
import com.example.diary.domain.model.workout.WorkoutSession
import com.example.diary.presentation.combined.list.EntryType
import java.time.LocalDateTime

sealed class DiaryEntry {
    abstract val time: LocalDateTime
    abstract val id: Long
    abstract val type: EntryType
    abstract val title: String
    abstract val subtitle: String?

    data class MealItem(
        val mealWithDetails: MealDetails
    ) : DiaryEntry() {
        override val time: LocalDateTime
            get() = mealWithDetails.meal.dateTime
        override val id: Long
            get() = mealWithDetails.meal.mealId
        override val type: EntryType = EntryType.MEAL
        override val title: String = mealWithDetails.meal.name

        override val subtitle: String?
            get() {
                // Список ингредиентов
                val ingredientsPart = mealWithDetails.ingredients
                    .joinToString(separator = ", ") { it.name }
                    .takeIf { it.isNotBlank() }

                // Читаемое имя способа приготовления
                val methodName = mealWithDetails.method.methodName
                    .takeIf { it.isNotBlank() }

                return buildString {
                    ingredientsPart?.let {
                        append(it)
                    }
                    methodName?.let {
                        if (isNotEmpty()) append("\n")
                        append("Способ: $it")
                    }
                }.takeIf { it.isNotBlank() }
            }
    }


    data class WaterItem(
        val waterIntake: WaterIntake,
        val categoryName: String             
    ) : DiaryEntry() {
        override val time = waterIntake.timestamp
        override val id   = waterIntake.intakeId
        override val type = EntryType.WATER
        override val title = categoryName
        override val subtitle = "Объем: ${waterIntake.amountMl} мл"  
    }

    


    data class SleepItem(
        val sleepSession: SleepSession
    ) : DiaryEntry() {
        override val time: LocalDateTime
            get() = sleepSession.startTime
        override val id: Long
            get() = sleepSession.sessionId
        override val type: EntryType = EntryType.SLEEP
        override val title: String = run {
            val duration = java.time.Duration.between(sleepSession.startTime, sleepSession.endTime)
            val hours = duration.toHours()
            val minutes = duration.minusHours(hours).toMinutes()
            "Продолжительность сна: ${hours}h ${minutes}m"
        }
        override val subtitle: String = "Качество сна: ${sleepSession.quality}/10"
    }

    


    data class StressItem(
        val stressLog: StressLog
    ) : DiaryEntry() {
        override val time: LocalDateTime
            get() = stressLog.timestamp
        override val id: Long
            get() = stressLog.logId
        override val type: EntryType = EntryType.STRESS
        override val title: String = "Стресс: ${stressLog.level}/10"
        override val subtitle: String? = null
    }

    


    data class SymptomItem(
        val symptom: Symptom,
        val typeName: String
    ) : DiaryEntry() {
        override val time = symptom.dateTime
        override val id = symptom.symptomId
        override val type = EntryType.SYMPTOM
        override val title = "$typeName"
        override val subtitle = "Интенсивность: ${symptom.intensity}"
    }

    data class WorkoutItem(
        val session: WorkoutSession,
        val workoutTypeName: String
    ) : DiaryEntry() {
        override val time = session.startTime
        override val id = session.sessionId
        override val type = EntryType.WORKOUT
        override val title = "$workoutTypeName"
        override val subtitle = "Интенсивность: ${session.intensity}/10"
    }

    data class MedicationItem(
        val medLog: MedicationLog
    ) : DiaryEntry() {
        override val time = medLog.scheduledTime
        override val id = medLog.id
        override val type = EntryType.MEDICATION
        override val title = "${medLog.name}"
        override val subtitle = "${medLog.dose}, Принято: ${if (medLog.taken) "✓" else "✗"}"
    }
}