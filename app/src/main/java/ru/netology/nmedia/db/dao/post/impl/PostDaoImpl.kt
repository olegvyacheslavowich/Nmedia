package ru.netology.nmedia.db.dao.post.impl

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.model.post.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {

    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARED} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARED_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEW_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIDEO_URL} TEXT NOT NULL DEFAULT 'https://www.youtube.com/watch?v=RFimmzu8nTw'
        );
        """.trimIndent()
    }

    object PostColumns {

        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_LIKES = "likes"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_SHARED = "shared"
        const val COLUMN_SHARED_COUNT = "sharedCount"
        const val COLUMN_VIEW_COUNT = "viewCount"
        const val COLUMN_VIDEO_URL = "videoUrl"

        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_LIKED_BY_ME,
            COLUMN_LIKES,
            COLUMN_PUBLISHED,
            COLUMN_SHARED,
            COLUMN_SHARED_COUNT,
            COLUMN_VIEW_COUNT,
            COLUMN_VIDEO_URL
        )
    }

    override fun getAll(): List<Post> {

        var posts = mutableListOf<Post>()

        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }

        return posts
    }

    override fun save(post: Post): Post {

        val values = ContentValues().apply {
            if (post.id != 0) {
                put(PostColumns.COLUMN_ID, post.id)
            }
            // TODO: remove hardcoded values
            put(PostColumns.COLUMN_AUTHOR, "Me")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, "now")
        }
        val id = db.replace(PostColumns.TABLE, null, values)
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }

    }

    override fun likeById(id: Int) {
        db.execSQL("""
            UPDATE posts SET
                likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
              WHERE id = ?
        """.trimIndent(), arrayOf(id))
    }

    override fun shareById(id: Int) {
        db.execSQL("""
            UPDATE posts SET
                shared = 1,
                sharedCount = sharedCount + 1
              WHERE id = ?
        """.trimIndent(), arrayOf(id))
    }

    override fun removeById(id: Int) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                liked = getBoolean(PostColumns.COLUMN_LIKED_BY_ME),
                likesCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                shared = getBoolean(PostColumns.COLUMN_SHARED),
                shareCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARED_COUNT)),
                viewCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEW_COUNT)),
                videoUrl = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO_URL))
            )
        }
    }

    private fun Cursor.getBoolean(columnName: String): Boolean =
        getInt(getColumnIndexOrThrow(columnName)) == 1
}