package com.team.dream.cantus.cross.dto.mapper

import com.team.dream.cantus.cross.dto.ApiTrackResponse
import com.team.dream.cantus.cross.model.DeezerTrack

class ApiTrackResponseMapper {

    fun map(trackResponse: ApiTrackResponse): List<DeezerTrack> {
        val albums = trackResponse.data

        return albums.map {
            DeezerTrack(
                it.id,
                it.title,
                it.titleShort,
                it.preview
            )
        }
    }
}