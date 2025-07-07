package com.example.diary.data.local.dao.meal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.diary.data.local.entity.meal.CookingMethod

@Dao
interface CookingMethodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCookingMethod(method: CookingMethod): Long
    @Query("SELECT * FROM cooking_methods")
    suspend fun getAllCookingMethods(): List<CookingMethod>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(methods: List<CookingMethod>)

}