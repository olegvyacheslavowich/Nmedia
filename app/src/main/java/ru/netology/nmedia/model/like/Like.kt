package ru.netology.nmedia.model.like

data class Like(
    val userId: Int?,
    val userName: String,
    val postId: Int,
    val postAuthor: String
)
