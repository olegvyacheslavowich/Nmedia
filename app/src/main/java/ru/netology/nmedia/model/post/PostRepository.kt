package ru.netology.nmedia.model.post

import java.lang.Exception

interface PostRepository {

    fun getAll(callback: GetAllCallback)
    fun getById(id: Int, callback: GetByIdCallback)
    fun likeById(id: Int, callback: GeneralCallback)
    fun shareById(id: Int)
    fun removeById(id: Int, callback: GeneralCallback)
    fun save(post: Post, callback: GeneralCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

    interface GetByIdCallback {
        fun onSuccess(post: Post)
        fun onError(e: Exception)
    }

    interface GeneralCallback {
        fun onSuccess()
        fun onError(e: Exception)
    }
}