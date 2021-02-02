package ru.netology.nmedia.model.post.impl

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository

class PostRepositoryInFileImpl(private val context: Context) : PostRepository {

    private var currentId = 1

    private val gson = Gson()
    private val fileName = "posts.json"
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            sync()
        }

    private val dataPosts = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(fileName)
        if (file.exists()) {
            context.openFileInput(fileName).bufferedReader().use {
                posts = gson.fromJson(it, type)
                dataPosts.value = posts
            }
        } else {
            sync()
        }
    }


    override fun getAll(): LiveData<List<Post>> = dataPosts

    override fun getById(id: Int): LiveData<Post> {
        return liveData<Post> {  }
    }


    override fun likeById(id: Int) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                liked = !it.liked,
                likesCount = if (!it.liked) it.likesCount + 1 else it.likesCount - 1
            )
        }
        dataPosts.value = posts
    }

    override fun shareById(id: Int) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shareCount = it.shareCount + 1)
        }
        dataPosts.value = posts
    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
        dataPosts.value = posts
    }

    override fun save(post: Post) {

        if (post.id == 0) {
            posts = listOf(
                post.copy(id = currentId++)
            ) + posts
            dataPosts.value = posts
        }

        posts = posts.map {
            if (post.id != it.id) it else it.copy(content = post.content)
        }

        dataPosts.value = posts
    }

    private fun sync() {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts, type))
        }
    }
}