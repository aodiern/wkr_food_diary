package com.example.diary.presentation.meal.model

import java.time.LocalDateTime

data class MealUiModel(
    val mealId: Long,
    val cookingMethodId: Long,
    val name: String,
    val method: String,
    val ingredients: List<String>,
    val ingredientIds: List<Long>,
    val dateTime: LocalDateTime
)