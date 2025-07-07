package com.example.diary.domain.usecase.water

import com.example.diary.domain.model.water.BeverageCategory
import com.example.diary.domain.repository.water.BeverageCategoryRepository
import kotlinx.coroutines.flow.Flow

class GetAllBeverageCategoriesUseCase(
    private val repository: BeverageCategoryRepository
) {
    operator fun invoke(): Flow<List<BeverageCategory>> =
        repository.getAllCategories()
}