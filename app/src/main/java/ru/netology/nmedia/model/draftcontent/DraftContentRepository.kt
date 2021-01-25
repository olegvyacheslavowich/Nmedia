package ru.netology.nmedia.model.draftcontent

interface DraftContentRepository {

    fun update(content: String)
    fun get(): String?
    fun remove()

}