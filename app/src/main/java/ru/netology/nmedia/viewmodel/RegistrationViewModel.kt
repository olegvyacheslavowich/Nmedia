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
class RegistrationViewModel @Inject constructor(
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