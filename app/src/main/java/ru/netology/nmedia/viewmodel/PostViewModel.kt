package ru.netology.nmedia.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.ad.impl.AdRepositoryInMemoryImpl
import ru.netology.nmedia.model.draftcontent.DraftContentRepository
import ru.netology.nmedia.model.post.PhotoModel
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.getEmptyPost
import ru.netology.nmedia.util.SingleLiveEvent
import ru.netology.nmedia.work.DeletePostWorker
import ru.netology.nmedia.work.SavePostWorker
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val draftContentRepository: DraftContentRepository,
    private val workManager: WorkManager,
    private val appAuth: AppAuth
) : ViewModel() {

    @ExperimentalCoroutinesApi
    val data: LiveData<FeedModel> = appAuth
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            repository.data
                .map { posts ->
                    FeedModel(
                        posts.map { it.copy(ownedByMe = (it.authorId == myId && myId != 0)) },
                        posts.isEmpty()
                    )
                }
        }.asLiveData(Dispatchers.Default)

    val authenticated = SingleLiveEvent<Boolean>()

    private val noPhoto = PhotoModel()
    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }


    private val adRepository = AdRepositoryInMemoryImpl()
    val error = MutableLiveData<String>()
    val adData = adRepository.getAll()

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _postState = MutableLiveData<FeedModelState>()
    val postState: LiveData<FeedModelState>
        get() = _postState

    val edited = MutableLiveData(getEmptyPost())
    var draftContent: LiveData<String> = draftContentRepository.get()

    init {
        loadPosts()
    }

    val newCount = data.switchMap {
        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0).asLiveData()
    }


    fun likeById(id: Int) = viewModelScope.launch {
        try {
            val isAuthenticated = appAuth.authStateFlow.value.id != 0
            if (!isAuthenticated) {
                authenticated.value = isAuthenticated
                return@launch
            }

            _dataState.value = FeedModelState(loading = true)
            repository.likeById(id)
            _dataState.value = FeedModelState(loading = false)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun removeById(id: Int) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)

            val data = workDataOf(DeletePostWorker.postId to id)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<DeletePostWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()
            workManager.enqueue(request)

            _dataState.value = FeedModelState(loading = false)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun save() {
        edited.value?.let {
            viewModelScope.launch {
                try {
                    _postState.value = FeedModelState(loading = true)

                    val post = it.copy(authorId = appAuth.authStateFlow.value.id)
                    val id = repository.saveWork(post, _photo.value?.file?.let { MediaUpload(it) })
                    val data = workDataOf(SavePostWorker.postId to id)

                    val constraint = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    val request = OneTimeWorkRequestBuilder<SavePostWorker>()
                        .setInputData(data)
                        .setConstraints(constraint)
                        .build()
                    workManager.enqueue(request)

                    _postState.value = FeedModelState(saved = true)
                    _postState.value = FeedModelState()
                    edited.value = getEmptyPost()
                    _photo.value = noPhoto
                } catch (e: Exception) {
                    _postState.value = FeedModelState(error = true)
                }
            }
        }
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun saveDraftContent(content: String) =
        viewModelScope.launch { draftContentRepository.update(content) }

    fun deleteDraftContent() = viewModelScope.launch { draftContentRepository.remove() }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun updateShowing() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.updateShow()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }
}

