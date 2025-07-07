package com.example.diary.domain.model.user

data class UserProfileModel(
    val fio: String,
    val dob: String,
    val diagnoses: String,
    val passwordHash: String?,
    val theme: String?
)