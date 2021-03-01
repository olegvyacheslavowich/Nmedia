package ru.netology.nmedia.model.post.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryHttpImpl : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}
    private val typeTokenPost = object : TypeToken<Post>() {}

    companion object {
        private const val BASE_URL = "http://85.115.173.83:9194"
        private val jsonType = "application/json".toMediaType()
    }


    override fun getAll(callback: PostRepository.GetAllCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .build()
        return client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: throw RuntimeException("body is null")
                try {
                    callback.onSuccess(gson.fromJson(body, typeToken.type))
                } catch (e: IOException) {
                    callback.onError(e)
                }
            }

        })
    }

    override fun getById(id: Int, callback: PostRepository.GetByIdCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/${id}")
            .build()
        return client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body =
                    response.body?.string() ?: throw RuntimeException("Post with ${id} not found")
                try {
                    callback.onSuccess(gson.fromJson(body, typeTokenPost.type))
                } catch (e: Exception) {
                    callback.onError(e)
                }
            }
        })
    }

    override fun likeById(id: Int, callback: PostRepository.GeneralCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/${id}/likes")
            .post("".toRequestBody(jsonType))
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (response.code > 400)
                            throw java.lang.RuntimeException(response.body.toString())
                        callback.onSuccess()
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })

    }

    override fun shareById(id: Int) {
    }

    override fun removeById(id: Int, callback: PostRepository.GeneralCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/${id}")
            .delete()
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (response.code > 400)
                            throw java.lang.RuntimeException(response.body.toString())
                        callback.onSuccess()
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })
    }

    override fun save(post: Post, callback: PostRepository.GeneralCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()
        return client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (response.code > 400)
                            throw java.lang.RuntimeException(response.body.toString())
                        callback.onSuccess()
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

            })
    }

}