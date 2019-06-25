package com.team.dream.cantus.cross.model


import com.squareup.moshi.Json

data class Tracks(
    @Json(name = "data")
    val `data`: List<DataX?>?
)