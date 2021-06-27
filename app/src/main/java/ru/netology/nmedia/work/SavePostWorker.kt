package ru.netology.nmedia.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.impl.PostRepositoryImpl

class SavePostWorker constructor(
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