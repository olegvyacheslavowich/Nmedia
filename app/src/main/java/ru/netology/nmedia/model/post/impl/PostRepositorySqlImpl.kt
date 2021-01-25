package ru.netology.nmedia.model.post.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.entity.PostEntity
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository

class PostRepositorySqlImpl(private val postDao: PostDao) : PostRepository {

    var posts = emptyList<Post>()
    val data = MutableLiveData(posts)

    init {
        data.value = posts
    }

    override fun getAll(): LiveData<List<Post>> = Transformations.map(postDao.getAll()) { list ->
        list.map {
            Post(
                it.id,
                it.author,
                it.published,
                it.content,
                it.liked,
                it.likesCount,
                it.shared,
                it.shareCount,
                it.viewCount,
                it.videoUrl
            )
        }
    }

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
                it.copy(shared = 1 == 1, shareCount = it.shareCount + 1)
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
        postDao.save(PostEntity.fromDto(post))
    }


}