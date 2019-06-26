package com.team.dream.cantus.cross.dto

import com.squareup.moshi.Json

data class ApiTrackResponse(
    @Json(name = "data")
    val data: List<ApiTrack>
)