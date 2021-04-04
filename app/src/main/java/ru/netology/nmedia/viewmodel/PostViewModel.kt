package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.api.posts.PostsApiService
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.model.ad.impl.AdRepositoryInMemoryImpl
import ru.netology.nmedia.model.draftcontent.impl.DraftContentRepositorySqlImpl
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.getEmptyPost
import ru.netology.nmedia.model.post.impl.PostRepositoryImpl
import java.lang.Exception

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val draftContentRepository = DraftContentRepositorySqlImpl(
        AppDatabase.getInstance(application).draftContentDao()
    )

    private val repository =
        PostRepositoryImpl(AppDatabase.getInstance(context = application).postDao())
    val data: LiveData<FeedModel> = repository.data.map(::FeedModel)

    private val adRepository = AdRepositoryInMemoryImpl()
    val error = MutableLiveData<String>()
    val adData = adRepository.getAll()

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState


    val edited = MutableLiveData(getEmptyPost())
    var draftContent: LiveData<String> = draftContentRepository.get()

    init {
        loadPosts()
    }


    fun likeById(id: Int) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.likeById(id)
            _dataState.value = FeedModelState(loading = false)
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun shareById(id: Int) {
        TODO()
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
        /*edited.value?.let {
            repository.save(it, object : PostRepository.GeneralCallback {
                override fun onSuccess() {
                    loadPosts()
                    edited.postValue(getEmptyPost())
                }

                override fun onError(e: Exception) {
                    error.postValue(e.toString())
                }
            })
        }*/
    }

    fun changeContent(content: String) {
//        edited.value?.let {
//            val text = content.trim()
//            if (it.content == text) {
//                return
//            }
//            edited.value = it.copy(content = text)
//        }
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
}