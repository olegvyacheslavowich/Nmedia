package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.model.auth.AuthRepository
import ru.netology.nmedia.model.draftcontent.DraftContentRepository
import ru.netology.nmedia.model.post.PostRepository

class ViewModelFactory(
    private val postRepository: PostRepository,
    private val contentRepository: DraftContentRepository,
    private val authRepository: AuthRepository,
    private val workManager: WorkManager,
    private val appAuth: AppAuth

) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(PostViewModel::class.java) -> {
                PostViewModel(postRepository, contentRepository, workManager, appAuth) as T
            }
            modelClass.isAssignableFrom(AuthorizationViewModel::class.java) -> {
                AuthorizationViewModel(appAuth, authRepository) as T
            }
            modelClass.isAssignableFrom(RegistrationViewModel::class.java) -> {
                RegistrationViewModel(appAuth, authRepository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(appAuth) as T
            }
            else -> {
                error("Unknown view model class")
            }
        }
}