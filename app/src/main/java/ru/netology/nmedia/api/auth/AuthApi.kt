package ru.netology.nmedia.api.auth

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.api.Api

object AuthApi : Api() {
    private const val BASE_URL = "${BuildConfig.BASE_URL}/api/"

    private val okhttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()


    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okhttp)
        .build()

    val retrofitService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }


}