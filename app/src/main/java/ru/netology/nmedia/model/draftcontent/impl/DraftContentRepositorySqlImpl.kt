package ru.netology.nmedia.model.draftcontent.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.db.dao.draftcontent.DraftContentDao
import ru.netology.nmedia.db.entity.DraftContentEntity
import ru.netology.nmedia.model.draftcontent.DraftContentRepository
import javax.inject.Inject

class DraftContentRepositorySqlImpl @Inject constructor(private val draftContentDao: DraftContentDao) :
    DraftContentRepository {

    override suspend fun update(content: String) {
        draftContentDao.update(DraftContentEntity.fromDto(content))
    }

    override fun get(): LiveData<String> =
        Transformations.map(draftContentDao.get()) { draftContent ->
            DraftContentEntity.toDto(draftContent ?: DraftContentEntity(""))
        }

    override suspend fun remove() {
        draftContentDao.removeAll()
    }

}