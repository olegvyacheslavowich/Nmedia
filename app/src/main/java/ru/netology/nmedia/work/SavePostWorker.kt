package ru.netology.nmedia.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.impl.PostRepositoryImpl

class SavePostWorker(
    private val postRepository: PostRepository,
    context: Context,
    params: WorkerParameters
) :
    CoroutineWorker(context, params) {

    companion object {
        const val postId = "PostId"
    }


    override suspend fun doWork(): Result {

        val id = inputData.getInt(postId, 0)
        if (id == 0) {
            return Result.failure()
        }

        postRepository.processWork(id)

        return Result.failure()
    }
}