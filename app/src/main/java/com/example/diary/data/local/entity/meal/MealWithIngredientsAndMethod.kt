package com.example.diary.data.local.entity.meal

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MealWithIngredientsAndMethod(
    @Embedded val meal: Meal,

    @Relation(
        parentColumn = "cookingMethodId",
        entityColumn = "methodId"
    )
    val cookingMethod: CookingMethod,

    @Relation(
        parentColumn = "mealId",
        entityColumn = "ingredientId",
        associateBy = Junction(MealIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
)
