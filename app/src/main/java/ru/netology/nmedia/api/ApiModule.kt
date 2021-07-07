package ru.netology.nmedia.api

import android.content.Context
import android.content.SharedPreferences
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.android.gms.common.GoogleApiAvailability
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.api.auth.AuthApiService
import ru.netology.nmedia.api.auth.token
import ru.netology.nmedia.api.posts.PostRemoteMediator
import ru.netology.nmedia.api.posts.PostsApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.entity.PostEntity
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    companion object {
        private const val BASE_URL = "${BuildConfig.BASE_URL}/api/"
    }

    @Provides
    fun providePrefs(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @PostOkHttp
    fun providePostOkhttp(prefs: SharedPreferences, logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                prefs.token?.let { token ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", token)
                        .build()
                    return@addInterceptor chain.proceed(newRequest)
                }
                chain.proceed(chain.request())
            }
            .build()

    @Provides
    @Singleton
    @AuthOkHttp
    fun provideAuthOkhttp(logging: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    @Provides
    @Singleton
    @PostRetrofit
    fun providePostRetrofit(@PostOkHttp okhttp: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okhttp)
        .build()

    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(@AuthOkHttp okhttp: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okhttp)
        .build()

    @Provides
    @Singleton
    fun provideRetrofitAuthService(@AuthRetrofit authRetrofit: Retrofit): AuthApiService =
        authRetrofit.create()

    @Provides
    @Singleton
    fun providePostApiService(@PostRetrofit postRetrofit: Retrofit): PostsApiService =
        postRetrofit.create()

    @Provides
    @Singleton
    fun provideAppAuth(
        prefs: SharedPreferences,
        retrofitAuthService: AuthApiService,
        postRepository: PostRepository
    ): AppAuth =
        AppAuth(prefs, retrofitAuthService, postRepository)

    @Provides
    fun provideGoogleApi(): GoogleApiAvailability = GoogleApiAvailability.getInstance()

    @ExperimentalPagingApi
    @Provides
    fun providePager(
        postsApiService: PostsApiService,
        postDao: PostDao
    ): Pager<Int, PostEntity> = Pager(
        config = PagingConfig(pageSize = 10),
        remoteMediator = PostRemoteMediator(postsApiService, postDao),
        pagingSourceFactory = postDao::getPagingSource
    )

}

@Qualifier
annotation class AuthOkHttp

@Qualifier
annotation class PostOkHttp

@Qualifier
annotation class AuthRetrofit

@Qualifier
annotation class PostRetrofit