package ru.netology.nmedia.db.dao.draftcontent

interface DraftContentDao {
    fun save(content: String)
    fun update(content: String)
    fun get(): String
    fun remove()
}