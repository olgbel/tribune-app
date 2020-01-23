package com.example.tribune_app.dto

data class UserModel(
    val id: Long,
    val username: String,
    var isReadOnly: Boolean = false,
    val badge: String? = null,
    val avatar: AttachmentModel?
)