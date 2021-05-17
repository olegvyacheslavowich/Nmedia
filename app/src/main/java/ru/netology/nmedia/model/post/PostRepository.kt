package ru.netology.nmedia.model.post

import kotlinx.coroutines.flow.Flow

interface PostRepository {

    val data: Flow<List<Post>>
    suspend fun getAll()
    fun getNewerCount(id: Int): Flow<Int>
    suspend fun likeById(id: Int)
    suspend fun shareById(id: Int)
    suspend fun removeById(id: Int)
    suspend fun save(post: Post)
    suspend fun updateShow()

}