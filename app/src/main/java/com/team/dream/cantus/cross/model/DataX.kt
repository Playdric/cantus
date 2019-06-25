package com.team.dream.cantus.cross.model


import com.squareup.moshi.Json

data class DataX(
    @Json(name = "artist")
    val artist: ArtistX?,
    @Json(name = "duration")
    val duration: String?,
    @Json(name = "explicit_content_cover")
    val explicitContentCover: Int?,
    @Json(name = "explicit_content_lyrics")
    val explicitContentLyrics: Int?,
    @Json(name = "explicit_lyrics")
    val explicitLyrics: Boolean?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "link")
    val link: String?,
    @Json(name = "preview")
    val preview: String?,
    @Json(name = "rank")
    val rank: String?,
    @Json(name = "readable")
    val readable: Boolean?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "title_short")
    val titleShort: String?,
    @Json(name = "title_version")
    val titleVersion: String?,
    @Json(name = "type")
    val type: String?
)