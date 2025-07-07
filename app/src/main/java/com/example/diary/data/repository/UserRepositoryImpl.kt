package com.example.diary.data.repository


import com.example.diary.data.local.dao.user.ProfileDao
import com.example.diary.data.mapper.toEntity
import com.example.diary.data.mapper.toModel
import com.example.diary.domain.model.user.UserProfileModel
import com.example.diary.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val profileDao: ProfileDao
) : UserRepository {

    override fun observeProfile(): Flow<UserProfileModel?> =
        profileDao.observeProfile()
            .map { it?.toModel() }


    override suspend fun saveProfile(profile: UserProfileModel) {
        profileDao.upsertProfile(profile.toEntity())
    }

}
