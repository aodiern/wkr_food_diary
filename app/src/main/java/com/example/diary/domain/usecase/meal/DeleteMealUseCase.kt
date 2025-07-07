package com.example.diary.domain.usecase.meal

import com.example.diary.data.local.entity.meal.Meal
import com.example.diary.domain.repository.meal.MealRepository

class DeleteMealUseCase(
    private val repository: MealRepository
) {
    suspend operator fun invoke(meal: Meal) {
        if (meal.mealId <= 0) {
            throw IllegalArgumentException("Некорректный ID блюда")
        }
        repository.deleteMeal(meal)
    }
}
