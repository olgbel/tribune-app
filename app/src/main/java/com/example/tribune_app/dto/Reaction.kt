package com.example.tribune_app.dto

enum class ReactionType {
    LIKE, DISLIKE
}

data class Reaction (
    val userId: Long,
    val date: Long,
    val type: ReactionType
)