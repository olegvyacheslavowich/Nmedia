package ru.netology.nmedia.db.dao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.db.dao.draftcontent.DraftContentDao
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.dao.work.PostWorkDao

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    fun provideWorkDao(appDatabase: AppDatabase): PostWorkDao = appDatabase.postWorkDao()

    @Provides
    fun providePostDao(appDatabase: AppDatabase): PostDao = appDatabase.postDao()

    @Provides
    fun provideDraftContentDao(appDatabase: AppDatabase): DraftContentDao =
        appDatabase.draftContentDao()


}