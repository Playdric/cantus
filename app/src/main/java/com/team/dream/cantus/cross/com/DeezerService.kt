package com.team.dream.cantus.cross.com

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerService {

    @GET("user/2529/albums")
    fun getAlbums(
        @Query("appid") appid: String,
        @Query("q") cityName: String
    ): Deferred<Response<Forecast>>
}