package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.auth.AuthRepository
import ru.netology.nmedia.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val appAuth: AppAuth,
    private val repository: AuthRepository
) : ViewModel() {

    val data: LiveData<AuthState> = appAuth.authStateFlow.asLiveData(Dispatchers.Default)

    private val _dataState = SingleLiveEvent<FeedModelState>()
    val dataState: SingleLiveEvent<FeedModelState>
        get() = _dataState

    fun login(login: String, password: String) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.login(login, password)
                .collect {
                    appAuth.setAuth(it.id, it.token ?: "")
                }
            _dataState.value = FeedModelState(loading = false)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }

    }

}