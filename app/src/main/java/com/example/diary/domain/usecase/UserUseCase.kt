package com.example.diary.domain.usecase

import com.example.diary.domain.model.user.UserFileModel
import com.example.diary.domain.model.user.UserProfileModel
import com.example.diary.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject




class ObserveProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<UserProfileModel?> =
        repository.observeProfile()
}




class SaveProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(profile: UserProfileModel) {
        repository.saveProfile(profile)
    }
}
