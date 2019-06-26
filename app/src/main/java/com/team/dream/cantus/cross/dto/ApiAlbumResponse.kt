package com.team.dream.cantus.cross.dto

import com.squareup.moshi.Json

data class ApiAlbumResponse(
    @Json(name = "data")
    val data: List<ApiAlbum>
)