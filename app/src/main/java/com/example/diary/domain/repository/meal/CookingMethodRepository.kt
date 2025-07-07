package com.example.diary.domain.repository.meal

import com.example.diary.data.local.entity.meal.CookingMethod

interface CookingMethodRepository {
    suspend fun getAllMethods(): List<CookingMethod>
    suspend fun insertMethod(method: CookingMethod): Long
    suspend fun insertAll(methods: List<CookingMethod>)
}