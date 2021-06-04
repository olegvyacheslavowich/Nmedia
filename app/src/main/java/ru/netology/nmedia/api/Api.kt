package ru.netology.nmedia.api

import okhttp3.logging.HttpLoggingInterceptor
import ru.netology.nmedia.BuildConfig

open class Api {

    protected val logging = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}