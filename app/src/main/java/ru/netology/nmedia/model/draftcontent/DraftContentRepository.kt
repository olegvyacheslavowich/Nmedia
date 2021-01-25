package ru.netology.nmedia.model.draftcontent

import androidx.lifecycle.LiveData

interface DraftContentRepository {

    fun update(content: String)
    fun get(): LiveData<String>
    fun remove()

}