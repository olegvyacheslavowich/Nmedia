package ru.netology.nmedia.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    companion object {
        private const val dbName = "app.db"
    }

    @Provides
    @Singleton
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .fallbackToDestructiveMigration()
            .build()

}