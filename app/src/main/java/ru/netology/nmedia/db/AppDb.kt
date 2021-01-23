package ru.netology.nmedia.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.netology.nmedia.db.dao.draftcontent.DraftContentDao
import ru.netology.nmedia.db.dao.draftcontent.impl.DraftContentDaoImpl
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.dao.post.impl.PostDaoImpl

class AppDb(db: SQLiteDatabase) {

    val postDao: PostDao = PostDaoImpl(db)
    val draftContentDao = DraftContentDaoImpl(db)

    companion object {
        private const val dbName = "app.db"
        private const val dbVersion = 2

        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: AppDb(buildDatabase(context, arrayOf(PostDaoImpl.DDL, DraftContentDaoImpl.DDL)))
            }
        }

        private fun buildDatabase(context: Context, DDLs: Array<String>) =
            DbHelper(context, dbName, DDLs, dbVersion).writableDatabase
    }


}

class DbHelper(context: Context, dbName: String, private val DDLs: Array<String>, dbVersion: Int) :
    SQLiteOpenHelper(context, dbName, null, dbVersion) {

    override fun onCreate(db: SQLiteDatabase) {
        DDLs.forEach {
            db.execSQL(it)
        }
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (newVersion == 2) {
            db?.execSQL(DDLs[1])
        }
    }

}