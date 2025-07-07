package com.example.diary.domain.usecase.meal

import com.example.diary.domain.repository.meal.MealIngredientRepository
import javax.inject.Inject

class DeleteMealIngredientUseCase @Inject constructor(
    private val repository: MealIngredientRepository
) {
    suspend operator fun invoke(mealId: Long, ingredientId: Long) {
        repository.deleteMealIngredientCrossRef(mealId, ingredientId)
    }
}