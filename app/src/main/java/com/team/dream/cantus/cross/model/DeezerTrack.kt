package com.team.dream.cantus.cross.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeezerTrack(
        val id: Int,
        val title: String,
        val titleShort: String,
        val preview: String
): Parcelable