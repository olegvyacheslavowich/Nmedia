package ru.netology.nmedia.di

import android.content.Context
import androidx.room.Room
import androidx.work.Configuration
import androidx.work.WorkManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.api.auth.AuthApiService
import ru.netology.nmedia.api.auth.token
import ru.netology.nmedia.api.posts.PostsApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.model.auth.AuthRepositoryImpl
import ru.netology.nmedia.model.draftcontent.impl.DraftContentRepositorySqlImpl
import ru.netology.nmedia.model.post.impl.PostRepositoryImpl
import ru.netology.nmedia.viewmodel.ViewModelFactory
import ru.netology.nmedia.work.WorkFactoryDelegate

class DependencyContainer private constructor(private val context: Context) {

    companion object {
        private const val BASE_URL = "${BuildConfig.BASE_URL}/api/"
        private const val dbName = "app.db"

        @Volatile
        private var instance: DependencyContainer? = null

        fun getInstance(context: Context): DependencyContainer {
            return instance ?: synchronized(this) {
                instance ?: DependencyContainer(context).also { instance = it }
            }
        }
    }

    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    private val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val postOkhttp = OkHttpClient.Builder()
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

    private val authOkhttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val postRetrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(postOkhttp)
        .build()

    private val authRetrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(authOkhttp)
        .build()

    private val retrofitAuthService: AuthApiService by lazy {
        authRetrofit.create(AuthApiService::class.java)
    }

    private val postApiService: PostsApiService by lazy {
        postRetrofit.create(PostsApiService::class.java)
    }

    private val db = Room.databaseBuilder(context, AppDatabase::class.java, dbName)
        .fallbackToDestructiveMigration()
        .build()


    private val postRepository = PostRepositoryImpl(db.postDao(), db.postWorkDao(), postApiService)
    private val authRepository = AuthRepositoryImpl(retrofitAuthService)
    private val draftContentRepository = DraftContentRepositorySqlImpl(db.draftContentDao())

    val workManager = run {
        WorkManager.initialize(
            context, Configuration.Builder()
                .setWorkerFactory(WorkFactoryDelegate(postRepository))
                .build()
        )
        WorkManager.getInstance(context)
    }

    val appAuth = AppAuth(prefs, retrofitAuthService)

    val viewModelFactory =
        ViewModelFactory(
            postRepository,
            draftContentRepository,
            authRepository,
            workManager,
            appAuth
        )

}