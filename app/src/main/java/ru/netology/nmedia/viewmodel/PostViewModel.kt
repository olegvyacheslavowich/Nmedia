package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.ad.impl.AdRepositoryInMemoryImpl
import ru.netology.nmedia.model.draftcontent.impl.DraftContentRepositorySqlImpl
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.getEmptyPost
import ru.netology.nmedia.model.post.impl.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val draftContentRepository = DraftContentRepositorySqlImpl(
        AppDatabase.getInstance(application).draftContentDao()
    )

    private val repository =
        PostRepositoryImpl(AppDatabase.getInstance(context = application).postDao())

    @ExperimentalCoroutinesApi
    val data: LiveData<FeedModel> = AppAuth.getInstance()
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
            val isAuthenticated = AppAuth.getInstance().authStateFlow.value.id != 0
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
            repository.removeById(id)
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
                    val post = it.copy(authorId = AppAuth.getInstance().authStateFlow.value.id)
                    repository.save(post)
                    _postState.value = FeedModelState(saved = true)
                    _postState.value = FeedModelState()
                    edited.value = getEmptyPost()
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