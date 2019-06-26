package com.team.dream.cantus.cross.com

import com.team.dream.cantus.cross.dto.ApiAlbumResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface DeezerService {

    @GET("user/2529/albums")
    fun getAlbums(
    ): Deferred<ApiAlbumResponse>
}