package com.example.diary.domain.model.user

import android.net.Uri

data class UserFileModel(
    val id: Long,
    val uri: Uri,
    val mimeType: String
)