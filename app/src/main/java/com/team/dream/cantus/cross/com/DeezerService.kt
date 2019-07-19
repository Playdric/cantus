package com.team.dream.cantus.cross.com

import com.team.dream.cantus.cross.dto.ApiAlbumResponse
import com.team.dream.cantus.cross.dto.ApiTrackResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerService {

    @GET("user/2529/albums")
    fun getAlbumsAsync(
            @Query("index")
            index: Int
    ): Deferred<ApiAlbumResponse>

    @GET("/album/{id}/tracks")
    fun getTracksAsync(
            @Path("id") id: Int
    ): Deferred<ApiTrackResponse>


    @GET("/search")
    fun getSearchAsync(
        @Query("q") search: String
    ): Deferred<ApiTrackResponse>
}