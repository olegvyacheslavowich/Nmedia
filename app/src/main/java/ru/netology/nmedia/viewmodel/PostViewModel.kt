package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.model.ad.impl.AdRepositoryInMemoryImpl
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.getEmptyPost
import ru.netology.nmedia.model.post.impl.PostRepositoryInFileImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryInFileImpl(application)
    private val adRepository = AdRepositoryInMemoryImpl()
    val dataList = repository.getAll()
    val adData = adRepository.getAll()
    var postData = MutableLiveData(getEmptyPost())
    val edited = MutableLiveData(getEmptyPost())

    fun likeById(id: Int) {
        repository.likeById(id)
        postData.value = repository.getById(id)
    }

    fun shareById(id: Int) {
        repository.shareById(id)
        postData.value = repository.getById(id)
    }

    fun removeById(id: Int) = repository.removeById(id)

    fun save() {
        edited.value?.let {
            repository.save(it)
            setPostData(it)
        }
        edited.value = getEmptyPost()
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

    fun setPostData(post: Post) {
        postData.value = post
    }
}