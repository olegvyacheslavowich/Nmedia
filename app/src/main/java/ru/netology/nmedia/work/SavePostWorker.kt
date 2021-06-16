package ru.netology.nmedia.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.model.post.impl.PostRepositoryImpl

class SavePostWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val appDatabase = AppDatabase.getInstance(context)

    companion object {
        const val postId = "PostId"
    }


    override suspend fun doWork(): Result {

        val id = inputData.getInt(postId, 0)
        if (id == 0) {
            return Result.failure()
        }

        val repository = PostRepositoryImpl(appDatabase.postDao(), appDatabase.postWorkDao())
        repository.processWork(id)

        return Result.failure()
    }
}