package com.androidpi.literefresh.sample.common.http

import com.androidpi.literefresh.sample.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

internal object RetrofitHttpClient {

    val okHttpClient = buildClient()

    fun buildClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(debugLoggingInterceptor())
        }
        return builder.build()
    }

    fun debugLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger
                { message ->
                    Timber.v(message)
                }
        ).setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}