package ru.netology.nmedia.model.post.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository

class PostRepositoryInMemoryImpl : PostRepository {

    private var currentId: Int = 1

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

    private var posts = listOf(
        Post(
            currentId++,
            "Oleg Karpenko",
            "12 december 2019",
            "Android is the most popular mobile platform",
            false,
            299_999,
            false,
            3,
            100_345
        ),
        Post(
            currentId++,
            "Natali Karpenko",
            "22 december 2019",
            "Android developers are needed in different areas: to make online banking with a complex degree of protection or an application for...",
            false,
            9_999,
            false,
            98,
            67
        ),
        Post(
            currentId++,
            "Alex Karpenko",
            "17 december 2021",
            "...for a soul mate, develop applications for learning English or a mobile service for finding flights.",
            false,
            87_299_999,
            false,
            7,
            238
        ),

        Post(
            currentId++,
            "Michel Jordam",
            "24 december 2020",
            "English or a mobile service for finding flights.",
            true,
            1_243_399,
            false,
            7,
            2_555_003
        ),

        Post(
            currentId++,
            "John Kalm",
            "26 december 2020",
            "Hello world.",
            true,
            1,
            true,
            7,
            1
        )
    )

    private val data = MutableLiveData(post)
    private val dataPosts = MutableLiveData(posts)

    override fun get(): LiveData<Post> = data
    override fun getAll(): LiveData<List<Post>> = dataPosts

    override fun like() {
        post = post.copy(
            liked = !post.liked,
            likesCount = if (!post.liked) post.likesCount + 1 else post.likesCount - 1
        )
        data.value = post
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

    override fun share() {
        post = post.copy(shareCount = post.shareCount + 1)
        data.value = post
    }

    override fun shareById(id: Int) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shareCount = it.shareCount + 1)
        }
        dataPosts.value = posts
    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id}
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

}