package com.androidpi.literefresh.sample.common.http

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientFactory {

    fun newRetrofit(baseUrl: String): Retrofit {
        return retrofitBuilder().baseUrl(baseUrl).build()
    }

    fun retrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
                .client(RetrofitHttpClient.okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

}
