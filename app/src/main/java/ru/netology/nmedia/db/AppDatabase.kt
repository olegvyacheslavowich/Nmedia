package ru.netology.nmedia.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.netology.nmedia.db.dao.draftcontent.DraftContentDao
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.entity.DraftContentEntity
import ru.netology.nmedia.db.entity.PostEntity

@Database(entities = [PostEntity::class, DraftContentEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun draftContentDao(): DraftContentDao

    companion object {
        private const val dbName = "app.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, dbName)
                .addMigrations(object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE posts ADD COLUMN authorAvatar TEXT DEFAULT '' NOT NULL");
                    }
                })
                .allowMainThreadQueries().
                build()
    }

}