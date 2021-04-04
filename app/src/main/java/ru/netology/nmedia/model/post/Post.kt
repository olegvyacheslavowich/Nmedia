package ru.netology.nmedia.model.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.netology.nmedia.db.entity.PostEntity


@Parcelize
data class Post(
    val id: Int,
    val author: String,
    val authorAvatar: String = "",
    val published: Long,
    val content: String,
    val videoUrl: Int = 0,
    val liked: Boolean = false,
    val likesCount: Int = 0,
    val shared: Boolean = false,
    val shareCount: Int = 0,
    val viewCount: Int = 0,
    val attachment: Attachment? = null
) : Parcelable

fun getEmptyPost(): Post {
    return Post(
        0,
        "Me",
        authorAvatar = "",
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

fun Post.toEntity(): PostEntity =
    PostEntity(
        this.id,
        this.author,
        this.authorAvatar,
        this.published,
        this.content,
        this.liked,
        this.likesCount,
        this.shared,
        this.shareCount,
        this.viewCount,
        this.videoUrl
    )

fun List<Post>.toEntity(): List<PostEntity> = map(Post::toEntity)




