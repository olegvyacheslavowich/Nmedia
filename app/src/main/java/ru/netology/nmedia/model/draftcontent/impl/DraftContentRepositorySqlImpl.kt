package ru.netology.nmedia.model.draftcontent.impl

import ru.netology.nmedia.db.dao.draftcontent.DraftContentDao
import ru.netology.nmedia.db.entity.DraftContentEntity
import ru.netology.nmedia.model.draftcontent.DraftContentRepository

class DraftContentRepositorySqlImpl(private val draftContentDao: DraftContentDao) :
    DraftContentRepository {

    override fun update(content: String) {
        draftContentDao.update(DraftContentEntity.fromDto(content))
    }

    override fun get(): String = DraftContentEntity.toDto(draftContentDao.get())

    override fun remove() {
        draftContentDao.removeAll()
    }

}