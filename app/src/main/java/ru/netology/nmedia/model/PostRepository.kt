package ru.netology.nmedia.model

import androidx.lifecycle.LiveData

interface PostRepository {

    fun get(): LiveData<Post>
    fun getAll(): LiveData<List<Post>>
    fun like()
    fun likeById(id: Int)
    fun share()
    fun shareById(id: Int)
}