package ru.netology.nmedia.work

import androidx.work.DelegatingWorkerFactory
import ru.netology.nmedia.model.post.PostRepository

class WorkFactoryDelegate(postRepository: PostRepository) : DelegatingWorkerFactory() {

    init {
        addFactory(DeletePostFactory(postRepository))
        addFactory(SavePostFactory(postRepository))
        addFactory(RefreshPostFactory(postRepository))
    }

}