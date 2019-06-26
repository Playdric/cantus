package com.team.dream.cantus.cross.model

data class DeezerAlbum(
    val id: Int,
    val title: String,
    val cover_medium: String,
    val nb_tracks: Int,
    val tracklist: String,
    val artistId: Int,
    val artistName: String)