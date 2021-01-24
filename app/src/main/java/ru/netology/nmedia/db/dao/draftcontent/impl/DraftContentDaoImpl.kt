package ru.netology.nmedia.db.dao.draftcontent.impl

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.db.dao.draftcontent.DraftContentDao

class DraftContentDaoImpl(private val db: SQLiteDatabase) : DraftContentDao {

    companion object {

        val DDL = """
            CREATE TABLE ${DraftContentColumns.TABLE} (
                ${DraftContentColumns.COLUMN_TEXT} TEXT NOT NULL                
            );
        """.trimIndent()

    }

    object DraftContentColumns {
        const val TABLE = "draft_content"
        const val COLUMN_TEXT = "text"

        val ALL_COLUMNS = arrayOf(COLUMN_TEXT)
    }

    override fun save(content: String) {
        db.insert(
            DraftContentColumns.TABLE,
            null,
            ContentValues().apply {
                this.put(DraftContentColumns.COLUMN_TEXT, content)
            }
        )
    }

    override fun update(content: String) {

        val queries = arrayOf(
            "DELETE FROM ${DraftContentColumns.TABLE}",
            "INSERT INTO ${DraftContentColumns.TABLE} VALUES ('${content}')"
        )

        queries.forEach {
            db.execSQL(it)
        }
    }

    override fun get(): String {

        var content = ""

        db.query(
            DraftContentColumns.TABLE,
            DraftContentColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            null,
        ).use {
            if (it.moveToNext()) {
                content = map(it)
            }
        }

        return content
    }

    override fun remove() {
        db.delete(
            DraftContentColumns.TABLE,
            null,
            null
        )
    }

    private fun map(cursor: Cursor): String {
        return cursor.getString(cursor.getColumnIndexOrThrow(DraftContentColumns.COLUMN_TEXT))
    }

}