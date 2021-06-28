package ru.netology.nmedia.work

import android.content.Context
import android.icu.util.Calendar
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.impl.PostRepositoryImpl

class RefreshPostsWorker constructor(
    private val postRepository: PostRepository,
    context: Context,
    params: WorkerParameters
) :
    CoroutineWorker(context, params) {

    private val sp = applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)

    companion object {
        const val name = "ru.netology.work.RefreshPostsWorker"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {

        try {
            postRepository.getAll()
            with(sp.edit()) {
                putString("date", "success ${Calendar.getInstance().time}")
                apply()
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            with(sp.edit()) {
                putString("date", "failure ${Calendar.getInstance().time}")
                apply()
            }
            Result.failure()
        }
    }
}