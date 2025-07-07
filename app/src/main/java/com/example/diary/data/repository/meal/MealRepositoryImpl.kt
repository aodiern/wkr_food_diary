package com.example.diary.data.repository.meal

import com.example.diary.data.local.dao.meal.MealDao
import com.example.diary.data.local.entity.meal.Meal
import com.example.diary.data.mapper.toDomain
import com.example.diary.domain.model.MealDetails
import com.example.diary.domain.repository.meal.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MealRepositoryImpl(
    private val mealDao: MealDao
) : MealRepository {

    override suspend fun insertMeal(meal: Meal): Long {
        return mealDao.insertMeal(
            Meal(
                mealId = meal.mealId,
                name = meal.name,
                cookingMethodId = meal.cookingMethodId,
                dateTime         = meal.dateTime
            )
        )
    }

    override fun getAllMealsWithDetailsFlow(): Flow<List<MealDetails>> {
        return mealDao.getAllMealsWithDetails()
            .map { entityList ->
                entityList.map { it.toDomain() }
            }
    }

    override suspend fun getAllMealsWithDetails(): List<MealDetails> {
        return mealDao.getAllMealsWithDetails().first().map { it.toDomain() }
    }

    override suspend fun updateMeal(meal: Meal) {
        mealDao.updateMeal(meal)
    }

    override suspend fun deleteMeal(meal: Meal) {
        mealDao.deleteMeal(meal)
    }
}