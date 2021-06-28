package ru.netology.nmedia.model.draftcontent

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.model.draftcontent.impl.DraftContentRepositorySqlImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DraftContentModule {

    @Binds
    @Singleton
    fun bindDraftContent(
        draftContentRepositorySqlImpl: DraftContentRepositorySqlImpl): DraftContentRepository

}