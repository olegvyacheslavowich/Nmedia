package ru.netology.nmedia.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.netology.nmedia.model.post.PostRepository

class DeletePostWorker constructor(
    private val postRepository: PostRepository,
    context: Context,
    params: WorkerParameters
) :
    CoroutineWorker(context, params) {

    companion object {
        const val name = "ru.netology.work.DeletePostWorker"
        const val postId = "postId"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {

        val id = inputData.getInt(postId, 0)
        if (id == 0) {
            Result.failure()
        }

        try {
            postRepository.removeById(id)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}