package ru.netology.nmedia.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.impl.PostRepositoryImpl

class DeletePostWorker(
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