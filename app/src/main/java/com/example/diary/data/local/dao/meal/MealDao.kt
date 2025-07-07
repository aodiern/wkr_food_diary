package com.example.diary.data.local.dao.meal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.diary.data.local.entity.meal.Meal
import com.example.diary.data.local.entity.meal.MealWithIngredientsAndMethod
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal): Long

    @Transaction
    @Query("SELECT * FROM meals")
    fun getAllMealsWithDetails(): Flow<List<MealWithIngredientsAndMethod>>
    @Update
    suspend fun updateMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)
}