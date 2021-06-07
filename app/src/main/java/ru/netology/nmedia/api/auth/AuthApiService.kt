package ru.netology.nmedia.api.auth

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.model.auth.PushToken

interface AuthApiService {

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun updateUser(
        @Field("login") login: String,
        @Field("pass") password: String
    ): Response<AuthState>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun register(
        @Field("name") name: String,
        @Field("login") login: String,
        @Field("pass") pass: String
    ): Response<AuthState>

    @POST("users/push-tokens")
    suspend fun savePushToken(@Body pushToken: PushToken)


}