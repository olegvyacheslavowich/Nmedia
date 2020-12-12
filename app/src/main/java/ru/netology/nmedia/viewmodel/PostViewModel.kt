package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.model.ad.impl.AdRepositoryInMemoryImpl
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.impl.PostRepositoryInMemoryImpl

class PostViewModel: ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    private val adRepository = AdRepositoryInMemoryImpl()
    val data = repository.get()
    val dataList = repository.getAll()
    val adData = adRepository.getAll()

    fun like() = repository.like()

    fun likeById(id: Int) = repository.likeById(id)

    fun share() = repository.share()

    fun shareById(id: Int) = repository.shareById(id)


}