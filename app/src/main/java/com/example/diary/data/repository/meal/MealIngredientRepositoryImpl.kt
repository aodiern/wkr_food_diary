package com.example.diary.data.repository.meal

import com.example.diary.data.local.dao.meal.MealIngredientDao
import com.example.diary.data.local.entity.meal.MealIngredientCrossRef
import com.example.diary.domain.repository.meal.MealIngredientRepository

class MealIngredientRepositoryImpl(
    private val dao: MealIngredientDao
) : MealIngredientRepository {

    override suspend fun addIngredientToMeal(mealId: Long, ingredientId: Long) {
        dao.insertCrossRef(MealIngredientCrossRef(mealId, ingredientId))
    }

    override suspend fun deleteMealIngredientCrossRef(mealId: Long, ingredientId: Long) {
        dao.deleteCrossRef(mealId, ingredientId)
    }

    override suspend fun deleteAllIngredientsFromMeal(mealId: Long) {
        dao.deleteByMealId(mealId)
    }

    override suspend fun getIngredientIdsForMeal(mealId: Long): List<Long> =
        dao.getIngredientIdsForMeal(mealId)

    override suspend fun getMealIdsForIngredient(ingredientId: Long): List<Long> =
        dao.getMealIdsForIngredient(ingredientId)
}

