package ru.netology.nmedia.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia.db.dao.draftcontent.DraftContentDao
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.entity.DraftContentEntity
import ru.netology.nmedia.db.entity.PostEntity

@Database(entities = [PostEntity::class, DraftContentEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun draftContentDao(): DraftContentDao

    companion object {
        private const val dbName = "app.db"

        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, dbName)
                .allowMainThreadQueries()
                .build()
    }
}