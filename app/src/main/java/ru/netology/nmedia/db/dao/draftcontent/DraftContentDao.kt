package ru.netology.nmedia.db.dao.draftcontent

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.db.entity.DraftContentEntity

@Dao
interface DraftContentDao {

    @Insert
    suspend fun save(draftContentEntity: DraftContentEntity)

    @Query("DELETE FROM draft_content")
    suspend fun removeAll()


    suspend fun update(draftContentEntity: DraftContentEntity) {
        removeAll()
        save(draftContentEntity)
    }

    @Query("SELECT text FROM draft_content")
    fun get(): LiveData<DraftContentEntity>

}