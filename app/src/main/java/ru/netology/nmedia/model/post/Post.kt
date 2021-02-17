package ru.netology.nmedia.model.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Post (
    val id: Int,
    val author: String,
    val published: Long,
    val content: String,
    val videoUrl: Int = 0,
    val liked: Boolean = false,
    val likesCount: Int = 0,
    val shared: Boolean = false,
    val shareCount: Int = 0,
    val viewCount: Int = 0
) : Parcelable

fun getEmptyPost(): Post {
    return Post(
        0,
        "Me",
        1613415363,
        "",
        //"https://www.youtube.com/watch?v=RFimmzu8nTw",
        0,
        false,
        0,
        false,
        0,
        0
    )
}




