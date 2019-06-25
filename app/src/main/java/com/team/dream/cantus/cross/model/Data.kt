package com.team.dream.cantus.cross.model


import com.squareup.moshi.Json

data class Data(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "picture")
    val picture: String?,
    @Json(name = "type")
    val type: String?
)