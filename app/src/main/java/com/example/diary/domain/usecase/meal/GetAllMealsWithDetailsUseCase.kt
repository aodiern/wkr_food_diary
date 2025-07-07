package com.example.diary.domain.usecase.meal

import com.example.diary.domain.model.MealDetails
import com.example.diary.domain.repository.meal.MealRepository
import kotlinx.coroutines.flow.Flow

class GetAllMealsWithDetailsUseCase(
    private val repository: MealRepository
) {
    operator fun invoke(): Flow<List<MealDetails>> =
        repository.getAllMealsWithDetailsFlow()
}