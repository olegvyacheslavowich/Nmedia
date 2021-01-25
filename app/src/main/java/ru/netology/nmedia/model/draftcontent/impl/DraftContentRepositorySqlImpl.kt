package ru.netology.nmedia.model.draftcontent.impl

import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.db.dao.draftcontent.DraftContentDao
import ru.netology.nmedia.db.entity.DraftContentEntity
import ru.netology.nmedia.model.draftcontent.DraftContentRepository

class DraftContentRepositorySqlImpl(private val draftContentDao: DraftContentDao) :
    DraftContentRepository {

    override fun update(content: String) {
        draftContentDao.update(DraftContentEntity.fromDto(content))
    }

    override fun get(): LiveData<String> = Transformations.map(draftContentDao.get()) {draftContent ->
        DraftContentEntity.toDto(draftContent?:DraftContentEntity(""))
    }

    override fun remove() {
        draftContentDao.removeAll()
    }

}