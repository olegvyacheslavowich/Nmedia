package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.auth.AuthRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.lang.Exception

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepositoryImpl()

    val data: LiveData<AuthState> =
        AppAuth.getInstance()
            .authStateFlow
            .asLiveData(Dispatchers.Default)

    private val _dataState = SingleLiveEvent<FeedModelState>()
    val dataState: SingleLiveEvent<FeedModelState>
        get() = _dataState


    fun register(name: String, login: String, pass: String) = viewModelScope.launch {

        _dataState.value = FeedModelState(loading = true)
        try {
            repository.register(name, login, pass)
                .collect {
                    AppAuth.getInstance().setAuth(it.id, it.token ?: "")
                }
            _dataState.value = FeedModelState(loading = false, saved = true)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }

    }


}