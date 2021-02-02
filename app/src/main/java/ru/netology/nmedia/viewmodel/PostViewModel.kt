package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.model.ad.impl.AdRepositoryInMemoryImpl
import ru.netology.nmedia.model.draftcontent.impl.DraftContentRepositorySqlImpl
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.getEmptyPost
import ru.netology.nmedia.model.post.impl.PostRepositorySqlImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositorySqlImpl(
        AppDatabase.getInstance(application).postDao()
    )
    private val draftContentRepository = DraftContentRepositorySqlImpl(
        AppDatabase.getInstance(application).draftContentDao()
    )

    private val adRepository = AdRepositoryInMemoryImpl()
    val dataList = repository.getAll()
    val adData = adRepository.getAll()
    var postData = liveData<Post>{}
    val edited = MutableLiveData(getEmptyPost())
    var draftContent: LiveData<String> = draftContentRepository.get()

    fun getById(id: Int) {
        postData = repository.getById(id)
    }

    fun likeById(id: Int) {
        repository.likeById(id)
    }

    fun shareById(id: Int) {
        repository.shareById(id)
    }

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

    fun saveDraftContent(content: String) = draftContentRepository.update(content)

    fun deleteDraftContent() = draftContentRepository.remove()
}