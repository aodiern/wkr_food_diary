package com.example.diary.domain.usecase.water

import com.example.diary.domain.model.water.BeverageCategory
import com.example.diary.domain.repository.water.BeverageCategoryRepository

class AddBeverageCategoryUseCase(
    private val repository: BeverageCategoryRepository
) {
    suspend operator fun invoke(category: BeverageCategory): Long =
        repository.addCategory(category)
}