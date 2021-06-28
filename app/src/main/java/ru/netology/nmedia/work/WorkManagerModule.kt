package ru.netology.nmedia.work

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.application.NMediaApplication
import ru.netology.nmedia.model.post.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WorkManagerModule {

    @Singleton
    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context,
        postRepository: PostRepository
    ): WorkManager =
        run {
            WorkManager.initialize(context, Configuration.Builder()
                .setWorkerFactory(WorkFactoryDelegate(postRepository))
                .build())
            WorkManager.getInstance(context)
        }

}
