package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.auth.AuthRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

class AuthorizationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepositoryImpl()

    val data: LiveData<AuthState> =
        AppAuth.getInstance()
            .authStateFlow
            .asLiveData(Dispatchers.Default)

    private val _dataState = SingleLiveEvent<FeedModelState>()
    val dataState: SingleLiveEvent<FeedModelState>
        get() = _dataState

    fun login(login: String, password: String) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.login(login, password)
                .collect {
                    AppAuth.getInstance().setAuth(it.id, it.token ?: "")
                }
            _dataState.value = FeedModelState(loading = false)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }

    }

}