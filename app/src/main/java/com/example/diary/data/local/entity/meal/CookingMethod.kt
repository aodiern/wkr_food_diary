package com.example.diary.data.local.entity.meal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cooking_methods")
data class CookingMethod(
    @PrimaryKey(autoGenerate = true) val methodId: Long = 0,
    val methodName: String
)