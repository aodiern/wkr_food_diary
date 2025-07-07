package com.example.diary.data.local.entity.water

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beverage_category")
data class BeverageCategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0L,
    val name: String
)