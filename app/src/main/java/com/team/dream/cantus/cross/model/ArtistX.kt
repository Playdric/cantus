package com.team.dream.cantus.cross.model


import com.squareup.moshi.Json

data class ArtistX(
    @Json(name = "id")
    val id: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "tracklist")
    val tracklist: String?,
    @Json(name = "type")
    val type: String?
)