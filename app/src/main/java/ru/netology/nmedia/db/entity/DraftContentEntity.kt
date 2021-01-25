package ru.netology.nmedia.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "draft_content")
data class DraftContentEntity(
    @PrimaryKey
    val text: String
) {
    companion object {
        fun fromDto(text: String) = DraftContentEntity(text)

        fun toDto(draftContentEntity: DraftContentEntity): String {
            return draftContentEntity.text
        }
    }
}

//fun DraftContentEntity.toDto(): String = this.text