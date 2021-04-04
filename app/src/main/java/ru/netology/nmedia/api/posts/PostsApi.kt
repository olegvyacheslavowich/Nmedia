package ru.netology.nmedia.api.posts

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.netology.nmedia.BuildConfig

object PostsApi {
    private const val BASE_URL = "${BuildConfig.BASE_URL}/api/"

    private val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okhttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()



    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okhttp)
        .build()

    val retrofitService: PostsApiService by lazy {
        retrofit.create(PostsApiService::class.java)
    }
}