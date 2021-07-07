package ru.netology.nmedia.model

import ru.netology.nmedia.model.post.Post
import java.util.Collections.emptyList

sealed interface FeedModel {
    val id: Int
}

data class PostModel(
    val post: Post,
    override val id: Int = post.id
) : FeedModel

data class AdModel(
    override val id: Int
) : FeedModel


data class FeedModelState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false,
    val saved: Boolean = false
)