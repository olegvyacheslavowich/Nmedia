package ru.netology.nmedia.application

import android.app.Application
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.work.RefreshPostsWorker
import java.util.concurrent.TimeUnit

class NMediaApplication : Application() {

    private val appScope = CoroutineScope(Dispatchers.Default)

    companion object {
        lateinit var container: DependencyContainer
    }

    override fun onCreate() {
        super.onCreate()
        container = DependencyContainer.getInstance(this)
        setupWork()

    }


    private fun setupWork() {
        appScope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = PeriodicWorkRequestBuilder<RefreshPostsWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
            DependencyContainer.getInstance(applicationContext).workManager.enqueueUniquePeriodicWork(
                RefreshPostsWorker.name,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }


}