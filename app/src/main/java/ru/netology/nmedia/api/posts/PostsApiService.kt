package ru.netology.nmedia.api.posts

import retrofit2.http.*
import ru.netology.nmedia.model.post.Post

interface PostsApiService {
    @GET("posts")
    suspend fun getAll(): List<Post>

    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Int): Post

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Int)

    @POST("posts/{id}/share")
    suspend fun shareById(@Path("id") id: Int)

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Int)

    @POST("posts")
    suspend fun save(@Body post: Post)
}