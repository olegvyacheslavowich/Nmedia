package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.model.PostRepository
import ru.netology.nmedia.model.impl.PostRepositoryInMemoryImpl

class PostViewModel: ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    val dataList = repository.getAll()

    fun like() = repository.like()

    fun likeById(id: Int) = repository.likeById(id)

    fun share() = repository.share()

    fun shareById(id: Int) = repository.shareById(id)


}