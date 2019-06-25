package com.team.dream.cantus.cross.model


import com.squareup.moshi.Json

data class Genres(
    @Json(name = "data")
    val `data`: List<Data?>?
)