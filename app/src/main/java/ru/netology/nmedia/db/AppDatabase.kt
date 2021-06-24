package ru.netology.nmedia.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.netology.nmedia.db.dao.draftcontent.DraftContentDao
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.dao.work.PostWorkDao
import ru.netology.nmedia.db.entity.DraftContentEntity
import ru.netology.nmedia.db.entity.PostEntity
import ru.netology.nmedia.db.entity.PostWorkEntity
import ru.netology.nmedia.model.post.AttachmentType

@Database(
    entities = [PostEntity::class, DraftContentEntity::class, PostWorkEntity::class],
    version = 3
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun draftContentDao(): DraftContentDao
    abstract fun postWorkDao(): PostWorkDao
}

class Converter() {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)

    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.toString()
}