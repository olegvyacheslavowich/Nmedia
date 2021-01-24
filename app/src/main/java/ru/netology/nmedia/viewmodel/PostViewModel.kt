package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.model.ad.impl.AdRepositoryInMemoryImpl
import ru.netology.nmedia.model.draftcontent.impl.DraftContentRepositorySqlImpl
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.getEmptyPost
import ru.netology.nmedia.model.post.impl.PostRepositoryInFileImpl
import ru.netology.nmedia.model.post.impl.PostRepositorySqlImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositorySqlImpl(
        AppDb.getInstance(application).postDao
    )
    private val draftContentRepository = DraftContentRepositorySqlImpl(
        AppDb.getInstance(application).draftContentDao
    )

    private val adRepository = AdRepositoryInMemoryImpl()
    val dataList = repository.getAll()
    val adData = adRepository.getAll()
    var postData = MutableLiveData(getEmptyPost())
    val edited = MutableLiveData(getEmptyPost())
    var draftContent: String
        get() = draftContentRepository.get()
        set(value) {
            draftContentRepository.update(value)
        }

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

    fun deleteDraftContent() = draftContentRepository.remove()
}