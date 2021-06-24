package ru.netology.nmedia.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import ru.netology.nmedia.model.post.PostRepository

class SavePostFactory(private val postRepository: PostRepository) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? =
        if (workerClassName == SavePostWorker::class.java.name) {
            SavePostWorker(postRepository, appContext, workerParameters)
        } else {
            null
        }
}