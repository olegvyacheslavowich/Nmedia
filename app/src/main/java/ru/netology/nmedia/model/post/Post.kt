package ru.netology.nmedia.model.post

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post (
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val liked: Boolean = false,
    val likesCount: Int = 0,
    val shared: Boolean = false,
    val shareCount: Int = 0,
    val viewCount: Int = 0,
    val videoUrl: String
) : Parcelable

fun getEmptyPost(): Post {
    return Post(
        0,
        "Me",
        "Now",
        "",
        false,
        0,
        false,
        0,
        0,
        "https://www.youtube.com/watch?v=RFimmzu8nTw"
    )
}




