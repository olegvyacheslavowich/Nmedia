package ru.netology.nmedia.api.posts

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.api.Api
import ru.netology.nmedia.auth.AppAuth

object PostsApi : Api() {
    private const val BASE_URL = "${BuildConfig.BASE_URL}/api/"

    private val okhttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            AppAuth.getInstance().authStateFlow.value.token?.let { token ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", token)
                    .build()
                return@addInterceptor chain.proceed(newRequest)
            }
            chain.proceed(chain.request())
        }
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