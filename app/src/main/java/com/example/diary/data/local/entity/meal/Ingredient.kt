package com.example.diary.data.local.entity.meal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val ingredientId: Long = 0,
    val name: String
)