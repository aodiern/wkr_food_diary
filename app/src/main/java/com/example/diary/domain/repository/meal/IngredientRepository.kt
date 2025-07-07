package com.example.diary.domain.repository.meal

import com.example.diary.data.local.entity.meal.Ingredient

interface IngredientRepository {
    suspend fun getAllIngredients(): List<Ingredient>
    suspend fun insertIngredient(ingredient: Ingredient): Long
    suspend fun insertOrGetIngredientByName(name: String): Long
    suspend fun updateIngredient(ingredient: Ingredient)
    suspend fun deleteIngredient(ingredient: Ingredient)
}