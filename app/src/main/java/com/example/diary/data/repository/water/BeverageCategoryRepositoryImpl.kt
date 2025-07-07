package com.example.diary.data.repository.water

import com.example.diary.data.local.dao.water.BeverageCategoryDao
import com.example.diary.data.mapper.toDomain
import com.example.diary.data.mapper.toEntity
import com.example.diary.domain.model.water.BeverageCategory
import com.example.diary.domain.repository.water.BeverageCategoryRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class BeverageCategoryRepositoryImpl @Inject constructor(
    private val dao: BeverageCategoryDao
) : BeverageCategoryRepository {
    override fun getAllCategories(): Flow<List<BeverageCategory>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun addCategory(category: BeverageCategory): Long =
        dao.insert(category.toEntity())

    override suspend fun deleteCategory(category: BeverageCategory) {
        dao.delete(category.toEntity())
    }
}
