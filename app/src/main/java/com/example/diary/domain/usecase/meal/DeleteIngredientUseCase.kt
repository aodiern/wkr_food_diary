package com.example.diary.domain.usecase.meal

import com.example.diary.data.local.entity.meal.Ingredient
import com.example.diary.domain.repository.meal.IngredientRepository

class DeleteIngredientUseCase(
    private val repository: IngredientRepository
) {
    suspend operator fun invoke(ingredient: Ingredient) {
        if (ingredient.ingredientId <= 0) {
            throw IllegalArgumentException("Некорректный ID ингредиента")
        }
        repository.deleteIngredient(ingredient)
    }
}