package ru.netology.nmedia.auth

import android.content.SharedPreferences
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.netology.nmedia.api.auth.AuthApiService
import ru.netology.nmedia.api.auth.token
import ru.netology.nmedia.api.posts.PostsApiService
import ru.netology.nmedia.model.auth.PushToken
import ru.netology.nmedia.model.post.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    private val prefs: SharedPreferences,
    private val apiService: AuthApiService,
    private val postRepository: PostRepository
) {

    private val idKey = "id"

    private val _authStateFlow: MutableStateFlow<AuthState>

    init {
        val id = prefs.getInt(idKey, 0)
        val token = prefs.token

        if (id == 0 || token == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(AuthState(id, token))
        }

    }

    val authStateFlow = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(id: Int, token: String) {
        _authStateFlow.value = AuthState(id, token)
        prefs.token = token
        with(prefs.edit()) {
            putInt(idKey, id)
            apply()
        }
        sendPushToken()
        CoroutineScope(Dispatchers.Default).launch {
            postRepository.getAll()
        }
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()

        with(prefs.edit()) {
            clear()
            commit()
        }
        sendPushToken()
        CoroutineScope(Dispatchers.Default).launch {
            postRepository.getAll()
        }
    }

    fun sendPushToken(token: String? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val pushToken = PushToken(token ?: Firebase.messaging.token.await())
                apiService.savePushToken(pushToken)
            } catch (e: Exception) {

            }
        }
    }
}

data class AuthState(val id: Int = 0, val token: String? = null)