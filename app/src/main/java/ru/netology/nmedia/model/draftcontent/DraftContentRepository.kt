package ru.netology.nmedia.model.draftcontent

import androidx.lifecycle.LiveData

interface DraftContentRepository {

    suspend fun update(content: String)
    fun get(): LiveData<String>
    suspend fun remove()

}