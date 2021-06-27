package ru.netology.nmedia.model.post

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.model.post.impl.PostRepositoryImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface PostRepositoryModule {

    @Binds
    @Singleton
    fun bindPostRepository(postRepositoryImpl: PostRepositoryImpl): PostRepository

}