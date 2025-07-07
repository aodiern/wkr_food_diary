package com.example.diary.data.local.dao.water

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.diary.data.local.entity.water.BeverageCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BeverageCategoryDao {
    @Insert
    suspend fun insert(category: BeverageCategoryEntity): Long

    @Query("SELECT * FROM beverage_category ORDER BY name")
    fun getAll(): Flow<List<BeverageCategoryEntity>>

    @Delete
    suspend fun delete(category: BeverageCategoryEntity)
}
