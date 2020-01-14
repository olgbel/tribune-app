package com.example.tribune_app.dto

data class CreatePostRequest(val content: String,
                             val linkURL: String?,
                             val attachment: AttachmentModel)