package ru.netology.nmedia.api.posts

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.nmedia.dto.Media
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
    suspend fun save(@Body post: Post): Response<Post>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Int): Response<List<Post>>

    @Multipart
    @POST("media")
    suspend fun upload(@Part media: MultipartBody.Part): Response<Media>

    suspend fun getAfter(
        @Path("id") id: Int,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("posts/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Post>>

    @GET("posts/{id}/before")
    suspend fun getBefore(@Path("id") id: Int, @Query("count") count: Int): Response<List<Post>>
}