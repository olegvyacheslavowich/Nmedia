package ru.netology.nmedia.model

import ru.netology.nmedia.model.post.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false
) {
}