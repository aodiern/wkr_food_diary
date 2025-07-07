package com.example.diary.domain.usecase.meal

import com.example.diary.data.local.entity.meal.Meal
import com.example.diary.domain.repository.meal.MealRepository

class UpdateMealUseCase(
    private val repository: MealRepository
) {
    suspend operator fun invoke(meal: Meal) {
        if (meal.mealId <= 0) {
            throw IllegalArgumentException("Некорректный ID блюда")
        }
        if (meal.name.isBlank()) {
            throw IllegalArgumentException("Название блюда не может быть пустым")
        }
        repository.updateMeal(meal)
    }
}
