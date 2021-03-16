package ru.netology.nmedia.api.posts

import retrofit2.Call
import retrofit2.http.*
import ru.netology.nmedia.model.post.Post

interface PostsApiService {
    @GET("/api/posts")
    fun getAll(): Call<List<Post>>

    @GET("{id}")
    fun getById(@Path("id") id: Int): Call<Post>

    @POST
    fun likeById(@Path("id") id: Int): Call<Any>

    @DELETE("{id}")
    fun removeById(@Path("id") id: Int): Call<Any>

    @POST("save")
    fun save(@Body post: Post): Call<Any>
}