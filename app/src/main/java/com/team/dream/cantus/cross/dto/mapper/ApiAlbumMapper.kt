package com.team.dream.cantus.cross.dto.mapper

import com.team.dream.cantus.cross.dto.ApiAlbum
import com.team.dream.cantus.cross.model.DeezerAlbum

class ApiAlbumMapper {
    fun map(apiAlbum: ApiAlbum): DeezerAlbum {
        return apiAlbum.run {

            DeezerAlbum(
                id,
                title,
                coverMedium,
                nbTracks,
                tracklist,
                artist.id,
                artist.name
            )
        }
    }
}