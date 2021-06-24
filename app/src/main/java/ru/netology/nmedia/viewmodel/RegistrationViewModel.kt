package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.auth.AuthRepository
import ru.netology.nmedia.model.auth.AuthRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.lang.Exception

class RegistrationViewModel(
    private val appAuth: AppAuth,
    private val repository: AuthRepository
) :
    ViewModel() {

    val data: LiveData<AuthState> = appAuth.authStateFlow.asLiveData(Dispatchers.Default)

    private val _dataState = SingleLiveEvent<FeedModelState>()
    val dataState: SingleLiveEvent<FeedModelState>
        get() = _dataState


    fun register(name: String, login: String, pass: String) = viewModelScope.launch {

        _dataState.value = FeedModelState(loading = true)
        try {
            repository.register(name, login, pass)
                .collect {
                    appAuth.setAuth(it.id, it.token ?: "")
                }
            _dataState.value = FeedModelState(loading = false, saved = true)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }

    }


}