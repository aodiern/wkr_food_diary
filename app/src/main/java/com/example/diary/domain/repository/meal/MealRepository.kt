package com.example.diary.domain.repository.meal

import com.example.diary.data.local.entity.meal.Meal
import com.example.diary.domain.model.MealDetails
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    suspend fun insertMeal(meal: Meal): Long
    suspend fun getAllMealsWithDetails(): List<MealDetails>
    fun getAllMealsWithDetailsFlow(): Flow<List<MealDetails>>
    suspend fun updateMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)
}