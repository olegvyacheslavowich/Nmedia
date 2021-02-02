package ru.netology.nmedia.db.dao.post

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.db.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun  getById(id: Int) : LiveData<PostEntity>

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE posts SET content = :content WHERE id = :id")
    fun updateContentById(id: Int, content: String)

    fun save(post: PostEntity) = if (post.id == 0) insert(post) else updateContentById(post.id, post.content)

    @Query("""
        UPDATE posts SET
                likesCount = likesCount + CASE WHEN liked THEN -1 ELSE 1 END,
                liked = CASE WHEN liked THEN 0 ELSE 1 END
              WHERE id = :id
              
    """)
    fun likeById(id: Int)

    @Query("""
        UPDATE posts SET
            shareCount = shareCount + 1
            WHERE id = :id
    """)
    fun shareById(id: Int)

    @Query("DELETE FROM posts WHERE id = :id")
    fun removeById(id: Int)
}