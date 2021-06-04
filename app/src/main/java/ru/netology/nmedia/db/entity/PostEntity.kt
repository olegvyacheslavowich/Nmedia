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
    val videoUrl: Int,
    val notSaved: Boolean = false,
    val needShow: Boolean = false,
    val authorId: Int = 0
)

fun PostEntity.toDto(): Post =
    Post(
        this.id,
        this.author,
        this.authorAvatar,
        this.published,
        this.content,
        this.videoUrl,
        this.liked,
        this.likesCount,
        this.shared,
        this.shareCount,
        this.viewCount,
        null,
        this.notSaved,
        this.needShow,
        false,
        this.authorId
    )


fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)

