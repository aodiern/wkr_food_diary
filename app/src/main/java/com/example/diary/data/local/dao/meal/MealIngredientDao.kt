package com.example.diary.data.local.dao.meal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.diary.data.local.entity.meal.MealIngredientCrossRef

@Dao
interface MealIngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: MealIngredientCrossRef)

    @Query("DELETE FROM meal_ingredient_cross_ref WHERE mealId=:m AND ingredientId=:i")
    suspend fun deleteCrossRef(m: Long, i: Long)

    @Query("SELECT ingredientId FROM meal_ingredient_cross_ref WHERE mealId=:m")
    suspend fun getIngredientIdsForMeal(m: Long): List<Long>

    @Query("DELETE FROM meal_ingredient_cross_ref WHERE mealId = :mealId")
    suspend fun deleteByMealId(mealId: Long)

    @Query("SELECT mealId FROM meal_ingredient_cross_ref WHERE ingredientId = :ingredientId")
    suspend fun getMealIdsForIngredient(ingredientId: Long): List<Long>

    @Query("SELECT * FROM meal_ingredient_cross_ref")
    suspend fun getAll(): List<MealIngredientCrossRef>
}