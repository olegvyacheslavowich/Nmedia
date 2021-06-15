package ru.netology.nmedia.model.post

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload

interface PostRepository {

    val data: Flow<List<Post>>
    suspend fun getAll()
    fun getNewerCount(id: Int): Flow<Int>
    suspend fun likeById(id: Int)
    suspend fun shareById(id: Int)
    suspend fun removeById(id: Int)
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, mediaUpload: MediaUpload)
    suspend fun updateShow()
    suspend fun upload(mediaUpload: MediaUpload): Media
    suspend fun saveWork(post: Post, mediaUpload: MediaUpload?): Int
    suspend fun processWork(id: Int)

}