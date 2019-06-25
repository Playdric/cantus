package com.team.dream.cantus.cross.com

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object DeezerRetrofitBuilder {

    fun getDeezerRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URLManager.getDeezer())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(getCommonHttpClientBuilder().build())
            .build()
    }

    private fun getCommonHttpClientBuilder(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
    }
}