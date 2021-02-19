package ru.netology.nmedia.model.post.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
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


    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .build()
        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let { gson.fromJson(it, typeToken.type) }
    }

    override fun getById(id: Int): Post {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/${id}")
            .build()
        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("Post with ${id} not found") }
            .let { gson.fromJson(it, typeTokenPost.type) }
    }

    override fun likeById(id: Int) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/${id}/likes")
            .post("".toRequestBody(jsonType))
            .build()
        client.newCall(request)
            .execute()
            .let { if (it.code > 400) throw java.lang.RuntimeException(it.body.toString()) }


    }

    override fun shareById(id: Int) {
    }

    override fun removeById(id: Int) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts/${id}")
            .delete()
            .build()
        client.newCall(request)
            .execute()
            .let { if (it.code > 400) throw java.lang.RuntimeException(it.body.toString()) }
    }

    override fun save(post: Post)  {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()
        return client.newCall(request)
            .execute()
            .let { if (it.code > 400) throw java.lang.RuntimeException(it.body.toString()) }
    }

}