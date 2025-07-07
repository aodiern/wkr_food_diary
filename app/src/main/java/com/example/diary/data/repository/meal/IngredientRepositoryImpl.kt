package com.example.diary.data.repository.meal

import com.example.diary.data.local.dao.meal.IngredientDao
import com.example.diary.data.local.entity.meal.Ingredient
import com.example.diary.domain.repository.meal.IngredientRepository

class IngredientRepositoryImpl(
    private val ingredientDao: IngredientDao
) : IngredientRepository {

    override suspend fun getAllIngredients(): List<Ingredient> {
        return ingredientDao.getAllIngredients()
    }

    override suspend fun insertIngredient(ingredient: Ingredient): Long {
        return ingredientDao.insertIngredient(ingredient)
    }

    override suspend fun insertOrGetIngredientByName(name: String): Long {
        val existing = ingredientDao.findByName(name)
        return existing?.ingredientId ?: ingredientDao.insertIngredient(Ingredient(name = name))
    }

    override suspend fun updateIngredient(ingredient: Ingredient) {
        ingredientDao.updateIngredient(ingredient)
    }

    override suspend fun deleteIngredient(ingredient: Ingredient) {
        ingredientDao.deleteIngredient(ingredient)
    }
}
