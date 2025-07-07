package com.example.diary.domain.usecase.meal

import com.example.diary.data.local.entity.meal.Meal
import com.example.diary.domain.repository.meal.MealRepository

class AddMealUseCase(private val repository: MealRepository) {
    suspend operator fun invoke(meal: Meal): Long {
        if (meal.name.isBlank()) throw IllegalArgumentException("Название обязательно")
        return repository.insertMeal(meal)
    }
}