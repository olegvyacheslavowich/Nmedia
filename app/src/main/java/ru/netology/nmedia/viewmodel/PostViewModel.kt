package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.model.ad.impl.AdRepositoryInMemoryImpl
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.getEmptyPost
import ru.netology.nmedia.model.post.impl.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    private val adRepository = AdRepositoryInMemoryImpl()
    val data = repository.get()
    val dataList = repository.getAll()
    val adData = adRepository.getAll()
    val edited = MutableLiveData(getEmptyPost())

    fun like() = repository.like()

    fun likeById(id: Int) = repository.likeById(id)

    fun share() = repository.share()

    fun shareById(id: Int) = repository.shareById(id)

    fun removeById(id: Int) = repository.removeById(id)

    fun save() {
        edited.value?.let {
            repository.save(it)
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
}