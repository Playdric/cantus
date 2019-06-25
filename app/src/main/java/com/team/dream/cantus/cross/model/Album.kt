package com.team.dream.cantus.cross.model


import com.squareup.moshi.Json

data class Album(
    @Json(name = "artist")
    val artist: Artist?,
    @Json(name = "available")
    val available: Boolean?,
    @Json(name = "contributors")
    val contributors: List<Contributor?>?,
    @Json(name = "cover")
    val cover: String?,
    @Json(name = "cover_big")
    val coverBig: String?,
    @Json(name = "cover_medium")
    val coverMedium: String?,
    @Json(name = "cover_small")
    val coverSmall: String?,
    @Json(name = "cover_xl")
    val coverXl: String?,
    @Json(name = "duration")
    val duration: Int?,
    @Json(name = "explicit_content_cover")
    val explicitContentCover: Int?,
    @Json(name = "explicit_content_lyrics")
    val explicitContentLyrics: Int?,
    @Json(name = "explicit_lyrics")
    val explicitLyrics: Boolean?,
    @Json(name = "fans")
    val fans: Int?,
    @Json(name = "genre_id")
    val genreId: Int?,
    @Json(name = "genres")
    val genres: Genres?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "link")
    val link: String?,
    @Json(name = "nb_tracks")
    val nbTracks: Int?,
    @Json(name = "rating")
    val rating: Int?,
    @Json(name = "record_type")
    val recordType: String?,
    @Json(name = "release_date")
    val releaseDate: String?,
    @Json(name = "share")
    val share: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "tracklist")
    val tracklist: String?,
    @Json(name = "tracks")
    val tracks: Tracks?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "upc")
    val upc: String?
)