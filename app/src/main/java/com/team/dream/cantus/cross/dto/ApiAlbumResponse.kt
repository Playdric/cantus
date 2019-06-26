package com.team.dream.cantus.cross.dto

import com.squareup.moshi.Json

data class ApiAlbumResponse(
    @field:Json(name = "data")
    val data: List<ApiAlbum>,
    @field:Json(name = "next")
    val next: String,
    @field:Json(name = "total")
    val total: Int
)