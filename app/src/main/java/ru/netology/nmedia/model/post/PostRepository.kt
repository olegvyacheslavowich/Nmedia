package ru.netology.nmedia.model.post

import androidx.lifecycle.LiveData

interface PostRepository {

    fun getAll(): LiveData<List<Post>>
    fun getById(id: Int): Post?
    fun likeById(id: Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun save(post: Post)
}