package com.example.diary.data.repository.meal

import com.example.diary.data.local.dao.meal.CookingMethodDao
import com.example.diary.data.local.entity.meal.CookingMethod
import com.example.diary.domain.repository.meal.CookingMethodRepository

class CookingMethodRepositoryImpl(
    private val methodDao: CookingMethodDao
) : CookingMethodRepository {

    override suspend fun getAllMethods(): List<CookingMethod> {
        return methodDao.getAllCookingMethods()
    }

    override suspend fun insertMethod(method: CookingMethod): Long {
        return methodDao.insertCookingMethod(method)
    }

    override suspend fun insertAll(methods: List<CookingMethod>) {
        methodDao.insertAll(methods)
    }
}
