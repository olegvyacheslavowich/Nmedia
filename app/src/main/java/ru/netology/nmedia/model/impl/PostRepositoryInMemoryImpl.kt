package ru.netology.nmedia.model.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.model.PostRepository
import ru.netology.nmedia.util.Util

class PostRepositoryInMemoryImpl: PostRepository {

    private var post = Post(
        0,
        "Natali Karpenko",
        "02 december 2020",
        "Android is the most popular mobile platform, so there is a shortage of Android developers: more than 500 vacancies appear every month (according to hh.ru). \\n\\nAndroid developers are needed in different areas: to make online banking with a complex degree of protection or an application for search for a soul mate, develop applications for learning English or a mobile service for finding flights.",
        false,
        10_299_999,
        false,
        0,
        44
    )

    private val data = MutableLiveData(post)

    override fun get():LiveData<Post> = data

    override fun like() {
        post = post.copy(liked = !post.liked, likesCount = if (!post.liked) post.likesCount + 1 else post.likesCount - 1)
        data.value = post
    }

    override fun share() {
        post = post.copy(shareCount = post.shareCount + 1)
        data.value = post
    }
}