package ru.netology.nmedia.model.post.impl

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository

class PostRepositoryInSharedPreferences(context: Context) : PostRepository {

    private val sharedPrefNamePosts = "postsRep"
    private val keyPosts = "posts"
    private var currentId = 1

    private val gson = Gson()
    private val sharedPrefs =
        context.getSharedPreferences(sharedPrefNamePosts, Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var posts: List<Post> = sharedPrefs.getString(keyPosts, null)?.let {
        gson.fromJson(it, type)
    } ?: emptyList()
        set(value) {
            field = value
            sync()
        }

    private val dataPosts = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = dataPosts

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
        sharedPrefs.edit().apply {
            putString(keyPosts, gson.toJson(posts))
            apply()
        }
    }
}