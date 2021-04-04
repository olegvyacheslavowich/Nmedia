package ru.netology.nmedia.model.post.impl

import androidx.lifecycle.map
import ru.netology.nmedia.api.posts.PostsApi
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.entity.PostEntity
import ru.netology.nmedia.db.entity.toDto
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.toEntity

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {

    override val data = dao.getAll().map(List<PostEntity>::toDto)
    override suspend fun getAll() {
        val all = PostsApi.retrofitService.getAll()
        dao.insert(all.toEntity())
    }

    override suspend fun likeById(id: Int) {
        PostsApi.retrofitService.likeById(id)
        dao.likeById(id)
    }

    override suspend fun shareById(id: Int) = PostsApi.retrofitService.shareById(id)
    override suspend fun removeById(id: Int) {
        dao.removeById(id)
        PostsApi.retrofitService.removeById(id)
    }

    override suspend fun save(post: Post) = PostsApi.retrofitService.save(post)

}
