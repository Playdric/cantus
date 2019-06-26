package com.team.dream.cantus.cross.dto


import com.squareup.moshi.Json


data class ApiAlbum(
    @field:Json(name = "artist")
    val artist: Artist,
    @field:Json(name = "available")
    val available: Boolean,
    @field:Json(name = "contributors")
    val contributors: List<Contributor>,
    @field:Json(name = "cover")
    val cover: String,
    @field:Json(name = "cover_big")
    val coverBig: String,
    @field:Json(name = "cover_medium")
    val coverMedium: String,
    @field:Json(name = "cover_small")
    val coverSmall: String,
    @field:Json(name = "cover_xl")
    val coverXl: String,
    @field:Json(name = "duration")
    val duration: Int,
    @field:Json(name = "explicit_content_cover")
    val explicitContentCover: Int,
    @field:Json(name = "explicit_content_lyrics")
    val explicitContentLyrics: Int,
    @field:Json(name = "explicit_lyrics")
    val explicitLyrics: Boolean,
    @field:Json(name = "fans")
    val fans: Int,
    @field:Json(name = "genre_id")
    val genreId: Int,
    @field:Json(name = "genres")
    val genres: Genres,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "label")
    val label: String,
    @field:Json(name = "link")
    val link: String,
    @field:Json(name = "nb_tracks")
    val nbTracks: Int,
    @field:Json(name = "rating")
    val rating: Int,
    @field:Json(name = "record_type")
    val recordType: String,
    @field:Json(name = "release_date")
    val releaseDate: String,
    @field:Json(name = "share")
    val share: String,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "tracklist")
    val tracklist: String,
    @field:Json(name = "tracks")
    val tracks: Tracks,
    @field:Json(name = "type")
    val type: String,
    @field:Json(name = "upc")
    val upc: String
) {
    data class Genres(
        @field:Json(name = "data")
        val `data`: List<Data>
    ) {
        data class Data(
            @field:Json(name = "id")
            val id: Int,
            @field:Json(name = "name")
            val name: String,
            @field:Json(name = "picture")
            val picture: String,
            @field:Json(name = "type")
            val type: String
        )
    }

    data class Artist(
        @field:Json(name = "id")
        val id: Int,
        @field:Json(name = "name")
        val name: String,
        @field:Json(name = "picture")
        val picture: String,
        @field:Json(name = "picture_big")
        val pictureBig: String,
        @field:Json(name = "picture_medium")
        val pictureMedium: String,
        @field:Json(name = "picture_small")
        val pictureSmall: String,
        @field:Json(name = "picture_xl")
        val pictureXl: String,
        @field:Json(name = "tracklist")
        val tracklist: String,
        @field:Json(name = "type")
        val type: String
    )

    data class Tracks(
        @field:Json(name = "data")
        val `data`: List<Data>
    ) {
        data class Data(
            @field:Json(name = "artist")
            val artist: Artist,
            @field:Json(name = "duration")
            val duration: Int,
            @field:Json(name = "explicit_content_cover")
            val explicitContentCover: Int,
            @field:Json(name = "explicit_content_lyrics")
            val explicitContentLyrics: Int,
            @field:Json(name = "explicit_lyrics")
            val explicitLyrics: Boolean,
            @field:Json(name = "id")
            val id: Int,
            @field:Json(name = "link")
            val link: String,
            @field:Json(name = "preview")
            val preview: String,
            @field:Json(name = "rank")
            val rank: Int,
            @field:Json(name = "readable")
            val readable: Boolean,
            @field:Json(name = "title")
            val title: String,
            @field:Json(name = "title_short")
            val titleShort: String,
            @field:Json(name = "title_version")
            val titleVersion: String,
            @field:Json(name = "type")
            val type: String
        ) {
            data class Artist(
                @field:Json(name = "id")
                val id: Int,
                @field:Json(name = "name")
                val name: String,
                @field:Json(name = "tracklist")
                val tracklist: String,
                @field:Json(name = "type")
                val type: String
            )
        }
    }

    data class Contributor(
        @field:Json(name = "id")
        val id: Int,
        @field:Json(name = "link")
        val link: String,
        @field:Json(name = "name")
        val name: String,
        @field:Json(name = "picture")
        val picture: String,
        @field:Json(name = "picture_big")
        val pictureBig: String,
        @field:Json(name = "picture_medium")
        val pictureMedium: String,
        @field:Json(name = "picture_small")
        val pictureSmall: String,
        @field:Json(name = "picture_xl")
        val pictureXl: String,
        @field:Json(name = "radio")
        val radio: Boolean,
        @field:Json(name = "role")
        val role: String,
        @field:Json(name = "share")
        val share: String,
        @field:Json(name = "tracklist")
        val tracklist: String,
        @field:Json(name = "type")
        val type: String
    )
}