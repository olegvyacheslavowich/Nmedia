package ru.netology.nmedia.model.post.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.entity.PostEntity
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository

class PostRepositorySqlImpl(private val postDao: PostDao) : PostRepository {

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

    override fun getById(id: Int): LiveData<Post> = Transformations.map(postDao.getById(id)) {
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

    override fun likeById(id: Int) {
        postDao.likeById(id)
    }

    override fun shareById(id: Int) {
        postDao.shareById(id)
    }

    override fun removeById(id: Int) {
        postDao.removeById(id)
    }

    override fun save(post: Post) {
        postDao.save(PostEntity.fromDto(post))
    }


}