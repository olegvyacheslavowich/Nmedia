package ru.netology.nmedia.db.dao.draftcontent

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DraftContentDao {

    @Insert
    fun save(content: String)

    @Delete
    fun remove()


    fun update(content: String) {
        remove()
        save(content)
    }

    @Query("SELECT text FROM DraftContentEntity")
    fun get(): String

}