package com.example.diary.domain.repository.water

import com.example.diary.domain.model.water.BeverageCategory
import kotlinx.coroutines.flow.Flow

interface BeverageCategoryRepository {
    fun getAllCategories(): Flow<List<BeverageCategory>>
    suspend fun addCategory(category: BeverageCategory): Long
    suspend fun deleteCategory(category: BeverageCategory)
}