package com.example.diary.domain.usecase.meal

import com.example.diary.data.local.entity.meal.Ingredient
import com.example.diary.domain.repository.meal.IngredientRepository

class GetAllIngredientsUseCase(
    private val repository: IngredientRepository
) {
    suspend operator fun invoke(): List<Ingredient> = repository.getAllIngredients()
}