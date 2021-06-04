package ru.netology.nmedia.model.post

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.netology.nmedia.db.entity.AttachmentEmbeddable

@Parcelize
data class Attachment(
    val url: String = "",
    val type: AttachmentType
) : Parcelable

enum class AttachmentType {
    IMAGE
}

fun Attachment.toEntity(): AttachmentEmbeddable =
    AttachmentEmbeddable(this.url, this.type)