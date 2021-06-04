package ru.netology.nmedia.model.post.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.netology.nmedia.api.posts.PostsApi
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.entity.PostEntity
import ru.netology.nmedia.db.entity.toDto
import ru.netology.nmedia.errors.ApiError
import ru.netology.nmedia.errors.AppError.Companion.from
import ru.netology.nmedia.model.post.*

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {

    override val data = dao.getAll()
        .map(List<PostEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        val all = PostsApi.retrofitService.getAll()
        dao.insert(all.toEntity(true))
    }

    override fun getNewerCount(id: Int): Flow<Int> = flow {
        while (true) {
            delay(10_000)
            val response = PostsApi.retrofitService.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntityNoShow())
            emit(body.size)
        }
    }.catch { e ->
        throw from(e)
    }.flowOn(Dispatchers.Default)


    override suspend fun likeById(id: Int) {
        PostsApi.retrofitService.likeById(id)
        dao.likeById(id)
    }

    override suspend fun shareById(id: Int) = PostsApi.retrofitService.shareById(id)
    override suspend fun removeById(id: Int) {
        dao.removeById(id)
        PostsApi.retrofitService.removeById(id)
    }

    override suspend fun save(post: Post) {
        try {
            val response = PostsApi.retrofitService.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: Exception) {
            throw  RuntimeException()
        }
    }

    override suspend fun updateShow() {
        dao.updateShow()
    }


}

