package com.example.diary.domain.model

import com.example.diary.data.local.entity.meal.CookingMethod
import com.example.diary.data.local.entity.meal.Ingredient
import com.example.diary.data.local.entity.meal.Meal

data class MealDetails(
    val meal: Meal,
    val method: CookingMethod,
    val ingredients: List<Ingredient>
)