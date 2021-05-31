package ru.netology.nmedia.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.netology.nmedia.db.dao.draftcontent.DraftContentDao
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.entity.DraftContentEntity
import ru.netology.nmedia.db.entity.PostEntity
import ru.netology.nmedia.model.post.AttachmentType

@Database(entities = [PostEntity::class, DraftContentEntity::class], version = 3)
@TypeConverters(Converter::class)
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
                .addMigrations(object : Migration(2, 3) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE posts ADD COLUMN notSaved INTEGER DEFAULT 0 NOT NULL");
                    }
                })
                .addMigrations(object : Migration(3, 4) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("ALTER TABLE posts ADD COLUMN needShow INTEGER DEFAULT 0 NOT NULL");
                    }
                })
                .build()
    }

}

class Converter() {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)

    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.toString()
}