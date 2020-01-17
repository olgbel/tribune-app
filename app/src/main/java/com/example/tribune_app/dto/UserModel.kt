package com.example.tribune_app.dto

data class UserModel(
    val id: Long,
    var username: String,
    var isReadOnly: Boolean = false,
    var badge: String? = null,
    var avatar: AttachmentModel?
    )