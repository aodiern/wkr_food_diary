package com.example.diary.domain.usecase.meal

import com.example.diary.domain.repository.meal.MealIngredientRepository

class AddMealIngredientUseCase(
    private val repository: MealIngredientRepository
) {
    suspend operator fun invoke(mealId: Long, ingredientId: Long) {
        if (mealId <= 0 || ingredientId <= 0)
            throw IllegalArgumentException("Некорректный ID")
        repository.addIngredientToMeal(mealId, ingredientId)
    }
}