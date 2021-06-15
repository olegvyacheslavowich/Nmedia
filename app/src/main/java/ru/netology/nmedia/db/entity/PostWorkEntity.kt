package ru.netology.nmedia.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.model.post.Attachment
import ru.netology.nmedia.model.post.AttachmentType
import ru.netology.nmedia.model.post.Post

@Entity
data class PostWorkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val postId: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: Long,
    val liked: Boolean,
    val likesCount: Int,
    @Embedded
    val attachment: AttachmentEmbeddable?,
    var uri: String? = null
) {

    companion object {

        fun fromDto(post: Post) = PostWorkEntity(
            0,
            post.id,
            post.authorId,
            post.author,
            post.authorAvatar,
            post.content,
            post.published,
            post.liked,
            post.likesCount,
            AttachmentEmbeddable.fromDto(post.attachment)
        )

    }

}


fun PostWorkEntity.toDto() = Post(
    this.postId,
    this.author,
    this.authorAvatar,
    this.published,
    this.content,
    0,
    this.liked,
    this.likesCount,
    false,
    0,
    100,
    Attachment(this.uri ?: "", AttachmentType.IMAGE)
)



