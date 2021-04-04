package ru.netology.nmedia.db.dao.post

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nmedia.db.entity.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getById(id: Int): LiveData<PostEntity>

    @Query("SELECT COUNT(*) == 0 FROM posts")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("UPDATE posts SET content = :content WHERE id = :id")
    suspend fun updateContentById(id: Int, content: String)

    suspend fun save(post: PostEntity) =
        if (post.id == 0) insert(post) else updateContentById(post.id, post.content)

    @Query(
        """
        UPDATE posts SET
                likesCount = likesCount + CASE WHEN liked THEN -1 ELSE 1 END,
                liked = CASE WHEN liked THEN 0 ELSE 1 END
              WHERE id = :id
              
    """
    )
    suspend fun likeById(id: Int)

    @Query(
        """
        UPDATE posts SET
            shareCount = shareCount + 1
            WHERE id = :id
    """
    )
    suspend fun shareById(id: Int)

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun removeById(id: Int)
}