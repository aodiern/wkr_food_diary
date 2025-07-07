package com.example.diary.domain.repository.meal

interface MealIngredientRepository {
    suspend fun addIngredientToMeal(mealId: Long, ingredientId: Long)
    suspend fun deleteMealIngredientCrossRef(mealId: Long, ingredientId: Long)
    suspend fun getIngredientIdsForMeal(mealId: Long): List<Long>
    suspend fun deleteAllIngredientsFromMeal(mealId: Long)
    suspend fun getMealIdsForIngredient(ingredientId: Long): List<Long>
}