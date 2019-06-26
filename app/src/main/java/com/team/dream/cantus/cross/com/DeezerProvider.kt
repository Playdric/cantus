package com.team.dream.cantus.cross.com

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.team.dream.cantus.BuildConfig
import com.team.dream.cantus.cross.dto.ApiAlbumResponse
import com.team.dream.cantus.cross.dto.mapper.ApiAlbumResponseMapper
import com.team.dream.cantus.cross.model.DeezerAlbum
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Exception

object DeezerProvider {

    private var service: DeezerService


    init {
        service = Retrofit.Builder().baseUrl(URLManager.getDeezer())
            .client(createOkHttpClient())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(DeezerService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }


    fun getAlbums(listener: Listener<List<DeezerAlbum>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val result: ApiAlbumResponse
            try {
                result = service.getAlbums().await()
                Log.d("COUCOU", "cover:${result.data[0].coverMedium}")
                withContext(Dispatchers.Main) {
                    listener.onSuccess(ApiAlbumResponseMapper().map(result))
                }

            } catch (e: Throwable) {
                withContext(Dispatchers.Main) {
                    listener.onError(e)
                }
            }
        }

    }


    interface Listener<T> {
        fun onSuccess(data: T)
        fun onError(t: Throwable)
    }
}