package ru.netology.nmedia.model.post.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository

class PostRepositorySqlImpl(private val postDao: PostDao) : PostRepository {

    var posts = emptyList<Post>()
    val data = MutableLiveData(posts)

    init {
        posts = postDao.getAll()
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun getById(id: Int): Post? = posts.filter { it.id == id }.firstOrNull()

    override fun likeById(id: Int) {
        postDao.likeById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                liked = !it.liked,
                likesCount = if (it.liked) it.likesCount - 1 else it.likesCount + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Int) {
        postDao.shareById(id)
        posts = posts.map {
            if (it.id == id) {
                it.copy(shared = 1==1, shareCount = it.shareCount + 1)
            } else {
                it
            }
        }
        data.value = posts
    }

    override fun removeById(id: Int) {
        postDao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {

        val id = post.id
        val savedPost = postDao.save(post)
        posts = if (post.id == 0) {
            listOf(savedPost) + posts
        } else {
            posts.map {
                if (it.id == id) savedPost else it
            }
        }

        data.value = posts
    }
}