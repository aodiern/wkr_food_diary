package com.example.diary.data.local.dao.meal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.diary.data.local.entity.meal.Ingredient

@Dao
interface IngredientDao {

    @Query("SELECT * FROM ingredients")
    suspend fun getAllIngredients(): List<Ingredient>
    @Query("SELECT * FROM ingredients WHERE name = :name LIMIT 1")
    suspend fun findByName(name: String): Ingredient?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient): Long
    @Update
    suspend fun updateIngredient(ingredient: Ingredient)
    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)
}