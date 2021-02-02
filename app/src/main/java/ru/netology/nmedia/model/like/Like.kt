package ru.netology.nmedia.model.like

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Int,
    val postAuthor: String
)
