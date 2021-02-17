package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.ad.impl.AdRepositoryInMemoryImpl
import ru.netology.nmedia.model.draftcontent.impl.DraftContentRepositorySqlImpl
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.getEmptyPost
import ru.netology.nmedia.model.post.impl.PostRepositoryHttpImpl
import java.io.IOException
import kotlin.concurrent.thread

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryHttpImpl()
    private val draftContentRepository = DraftContentRepositorySqlImpl(
        AppDatabase.getInstance(application).draftContentDao()
    )

    private val adRepository = AdRepositoryInMemoryImpl()
    val dataList = MutableLiveData<FeedModel>()

    val adData = adRepository.getAll()
    var postData = MutableLiveData(getEmptyPost())
    val edited = MutableLiveData(getEmptyPost())
    var draftContent: LiveData<String> = draftContentRepository.get()

    init {
        loadPosts()
    }

    fun getById(id: Int) {
        thread {
            postData.postValue(repository.getById(id))
        }

    }

    fun likeById(id: Int) {
        thread {
            try {
                repository.likeById(id)
                dataList.value?.let {
                    it.posts.map { post ->
                        if (post.id == id) {
                            post.copy(
                                id = post.id,
                                liked = !post.liked,
                                likesCount = post.likesCount + if (post.liked) -1 else 1
                            )
                        } else {
                            post
                        }
                    }
                }.also {
                    dataList.postValue(it?.let { posts ->
                        FeedModel(
                            posts = posts,
                            empty = posts.isEmpty()
                        )
                    })
                }
                postData.postValue(dataList.value?.posts?.filter { it.id == id }?.first())
            } catch (e: IOException) {
                dataList.postValue(dataList.value?.copy(error = true))
            }
        }
    }

    fun shareById(id: Int) {
        repository.shareById(id)
    }

    fun removeById(id: Int) {
        thread {
            try {
                repository.removeById(id)
                dataList.value?.posts
                    ?.filter { it.id != id }
                    .apply {
                        dataList.postValue(
                            this?.let {
                                FeedModel(posts = it, it.isEmpty())
                            })
                    }
            } catch (e: IOException) {
                dataList.value = dataList.value?.copy()
            }
        }
    }

    fun save() {
        thread {
            edited.value?.let {
                repository.save(it)
                loadPosts()
            }
            edited.postValue(getEmptyPost())
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

    fun saveDraftContent(content: String) = draftContentRepository.update(content)

    fun deleteDraftContent() = draftContentRepository.remove()

    fun loadPosts() {
        thread {
            dataList.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getAll()
                FeedModel(posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                FeedModel(error = true)
            }.also {
                dataList.postValue(it)
            }
        }
    }
}