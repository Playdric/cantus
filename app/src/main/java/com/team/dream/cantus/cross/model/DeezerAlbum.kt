package com.team.dream.cantus.cross.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeezerAlbum(
    val id: Int,
    val title: String,
    val cover_medium: String,
    val nb_tracks: Int,
    val tracklist: String,
    val artistId: Int,
    val artistName: String): Parcelable {

    override fun toString(): String {
        return title
    }
}