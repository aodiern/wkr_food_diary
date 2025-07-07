package com.example.diary.data.local.entity.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 0,
    val fio: String,
    val dob: String,
    val diagnoses: String,
    val passwordHash: String? = null,
    val theme: String? = null
)