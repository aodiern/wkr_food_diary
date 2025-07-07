package com.example.diary.data.local.entity.meal

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "meal_ingredient_cross_ref",
    primaryKeys = ["mealId", "ingredientId"],
    foreignKeys = [
        ForeignKey(
            entity = Meal::class,
            parentColumns = ["mealId"],
            childColumns = ["mealId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Ingredient::class,
            parentColumns = ["ingredientId"],
            childColumns = ["ingredientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["ingredientId"])
    ]
)
data class MealIngredientCrossRef(
    val mealId: Long,
    val ingredientId: Long
)






