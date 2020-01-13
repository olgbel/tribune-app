package com.example.tribune_app.dto

import com.example.tribune_app.utils.BASE_URL

enum class AttachmentType {
    IMAGE
}

data class AttachmentModel(val id: String, val type: AttachmentType) {
    val url
        get() = "$BASE_URL/api/v1/static/$id"
}

data class PostModel(
    val id: Long,
    val author: UserModel,
    var badge: String,
    val dtCreation: Int,
    val content: String,
    var likes: Set<Long>,
    var dislikes: Set<Long>,
    var views: Int,
    val linkURL: String? = null,
    val attachment: AttachmentModel?
) {
    var likeActionPerforming = false
    var dislikeActionPerforming = false
    var openLinkBtnClickListener = false
}



