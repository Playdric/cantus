package com.team.dream.cantus.cross.com

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.team.dream.cantus.cross.dto.ApiAlbumResponse
import com.team.dream.cantus.cross.dto.ApiTrackResponse
import com.team.dream.cantus.cross.dto.mapper.ApiAlbumResponseMapper
import com.team.dream.cantus.cross.dto.mapper.ApiTrackResponseMapper
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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


    fun getAlbums(index: Int, listener: Listener<List<DeezerAlbum>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val result: ApiAlbumResponse
            try {
                result = service.getAlbumsAsync(index).await()
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

    fun getTracks(tracklistId: Int, listener: Listener<List<DeezerTrack>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val result: ApiTrackResponse
            try {
                result = service.getTracksAsync(tracklistId).await()
                withContext(Dispatchers.Main) {
                    listener.onSuccess(ApiTrackResponseMapper().map(result))
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