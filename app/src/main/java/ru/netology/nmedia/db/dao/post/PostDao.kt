package ru.netology.nmedia.db.dao.post

import ru.netology.nmedia.model.post.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeById(id: Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
}