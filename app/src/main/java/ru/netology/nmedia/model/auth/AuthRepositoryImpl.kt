package ru.netology.nmedia.model.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.netology.nmedia.api.auth.AuthApiService
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.errors.ApiError
import ru.netology.nmedia.errors.AppError.Companion.from

class AuthRepositoryImpl(private val authApiService: AuthApiService) : AuthRepository {

    override suspend fun login(login: String, password: String): Flow<AuthState> = flow {
        val response = authApiService.updateUser(login, password)
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }
        val authState = response.body() ?: throw ApiError(response.code(), response.message())
        emit(authState)
    }.catch { e -> throw from(e) }
        .flowOn(Dispatchers.Default)

    override suspend fun register(name: String, login: String, password: String): Flow<AuthState> =
        flow {
            val response = authApiService.register(name, login, password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            emit(authState)
        }.catch { e -> throw from(e) }
            .flowOn(Dispatchers.Default)


}