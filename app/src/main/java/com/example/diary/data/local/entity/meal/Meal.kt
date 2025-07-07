package com.example.diary.data.local.entity.meal

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true) val mealId: Long = 0,
    val name: String,
    val cookingMethodId: Long,
    val dateTime: LocalDateTime
)