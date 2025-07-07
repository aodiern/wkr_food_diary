package com.example.diary.domain.repository

import com.example.diary.domain.model.user.UserFileModel
import com.example.diary.domain.model.user.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeProfile(): Flow<UserProfileModel?>
    suspend fun saveProfile(profile: UserProfileModel)

}
