package com.example.diary.domain.usecase.water

import com.example.diary.domain.model.water.BeverageCategory
import com.example.diary.domain.repository.water.BeverageCategoryRepository

class DeleteBeverageCategoryUseCase(
    private val repository: BeverageCategoryRepository
) {
    suspend operator fun invoke(category: BeverageCategory) =
        repository.deleteCategory(category)
}