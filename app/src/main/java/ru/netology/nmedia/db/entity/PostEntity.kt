package ru.netology.nmedia.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.model.post.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val author: String,
    val authorAvatar: String,
    val published: Long,
    val content: String,
    val liked: Boolean,
    val likesCount: Int,
    val shared: Boolean,
    val shareCount: Int,
    val viewCount: Int,
    val videoUrl: Int
) {
    companion object {

        fun fromDto(post: Post): PostEntity {
            return PostEntity(
                post.id,
                post.author,
                post.authorAvatar,
                post.published,
                post.content,
                post.liked,
                post.likesCount,
                post.shared,
                post.shareCount,
                post.viewCount,
                post.videoUrl
            )

        }

        fun toDto(post: PostEntity) =
            Post(
                post.id,
                post.author,
                post.authorAvatar,
                post.published,
                post.content,
                post.videoUrl,
                post.liked,
                post.likesCount,
                post.shared,
                post.shareCount,
                post.viewCount
            )

    }
}

