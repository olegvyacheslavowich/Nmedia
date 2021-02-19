package ru.netology.nmedia.model.post

interface PostRepository {

    fun getAll(): List<Post>
    fun getById(id: Int): Post
    fun likeById(id: Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun save(post: Post)
}