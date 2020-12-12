package ru.netology.nmedia.model.post

data class Post (
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val liked: Boolean = false,
    val likesCount: Int = 0,
    val shared: Boolean = false,
    val shareCount: Int = 0,
    val viewCount: Int = 0
)