package ru.netology.nmedia.model.draftcontent.impl

import ru.netology.nmedia.db.dao.draftcontent.DraftContentDao
import ru.netology.nmedia.model.draftcontent.DraftContentRepository

class DraftContentRepositorySqlImpl(private val draftContentDao: DraftContentDao) :
    DraftContentRepository {

    override fun update(content: String) {
        draftContentDao.update(content)
    }

    override fun get(): String = draftContentDao.get()

    override fun remove() {
        draftContentDao.remove()
    }


}