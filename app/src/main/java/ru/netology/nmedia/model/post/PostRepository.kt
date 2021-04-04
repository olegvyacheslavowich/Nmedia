package ru.netology.nmedia.model.post

import androidx.lifecycle.LiveData

interface PostRepository {

    val data: LiveData<List<Post>>
    suspend fun getAll()
    suspend fun likeById(id: Int)
    suspend fun shareById(id: Int)
    suspend fun removeById(id: Int)
    suspend fun save(post: Post)

}