package ru.netology.nmedia.model.post

import androidx.lifecycle.LiveData

interface PostRepository {

    fun get(): LiveData<Post>
    fun getAll(): LiveData<List<Post>>
    fun like()
    fun likeById(id: Int)
    fun share()
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun save(post: Post)
}