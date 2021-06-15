package ru.netology.nmedia.db.dao.work

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nmedia.db.entity.PostWorkEntity

@Dao
interface PostWorkDao {

    @Query("SELECT * FROM PostWorkEntity WHERE id = :id")
    suspend fun getById(id: Int): PostWorkEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(work: PostWorkEntity): Long

    @Query("DELETE FROM PostWorkEntity WHERE id = :id")
    suspend fun removeById(id: Int)


}