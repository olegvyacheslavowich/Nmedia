package ru.netology.nmedia.model.post.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.api.posts.PostsApi
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.entity.PostEntity
import ru.netology.nmedia.db.entity.toDto
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.errors.ApiError
import ru.netology.nmedia.errors.AppError
import ru.netology.nmedia.errors.NetworkError
import ru.netology.nmedia.errors.UnknownError
import ru.netology.nmedia.model.post.*
import java.io.IOException

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
        throw e
    }.flowOn(Dispatchers.IO)


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

    override suspend fun saveWithAttachment(post: Post, mediaUpload: MediaUpload) {
        try {
            val media = upload(mediaUpload)
            val postWithAttachment =
                post.copy(attachment = Attachment(media.id, AttachmentType.IMAGE))
            save(postWithAttachment)
        } catch (e: AppError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun updateShow() {
        dao.updateShow()
    }

    override suspend fun upload(mediaUpload: MediaUpload): Media {
        try {
            val media = MultipartBody.Part.createFormData(
                "file", mediaUpload.file.name, mediaUpload.file.asRequestBody()
            )
            val response = PostsApi.retrofitService.upload(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw NetworkError
        }
    }


}

