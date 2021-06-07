package ru.netology.nmedia.model.auth

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.dto.MediaUpload

interface AuthRepository {

    suspend fun login(login: String, password: String): Flow<AuthState>
    suspend fun register(name: String, login: String, password: String): Flow<AuthState>

}