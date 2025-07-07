package com.example.diary.domain.usecase.meal

import com.example.diary.data.local.entity.meal.CookingMethod
import com.example.diary.domain.repository.meal.CookingMethodRepository

class GetAllCookingMethodsUseCase(
    private val repository: CookingMethodRepository
) {
    suspend operator fun invoke(): List<CookingMethod> = repository.getAllMethods()
}