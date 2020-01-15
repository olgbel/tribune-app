package com.example.tribune_app.dto

data class UserModel(
    val id: Long,
    var username: String,
    var badge: String? = null,
    var avatar: AttachmentModel?
    )