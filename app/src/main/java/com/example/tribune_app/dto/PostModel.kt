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
    var likes: Set<Reaction>,
    var isLikedByMe: Boolean,
    var dislikes: Set<Reaction>,
    var isDislikedByMe: Boolean,
    var views: Int,
    val linkURL: String? = null,
    val attachment: AttachmentModel
) {
    var likeActionPerforming = false
    var dislikeActionPerforming = false

    fun updateLikes(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        likes = updatedModel.likes

        if (updatedModel.likes.filter { it.userId == author.id }.isNotEmpty()){
            isLikedByMe = true
        }
    }
    fun updateDislikes(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        dislikes = updatedModel.dislikes
        if (updatedModel.dislikes.filter { it.userId == author.id }.isNotEmpty()){
            isDislikedByMe = true
        }
    }
}



