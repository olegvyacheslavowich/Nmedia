package ru.netology.nmedia.model.post

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Attachment(
    val url: String = "",
    val description: String = "",
    val type: String = "IMAGE"
) : Parcelable
