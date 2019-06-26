package com.team.dream.cantus.cross.dto.mapper

import com.team.dream.cantus.cross.dto.ApiAlbum
import com.team.dream.cantus.cross.dto.ApiAlbumResponse
import com.team.dream.cantus.cross.model.DeezerAlbum

class ApiAlbumResponseMapper {
    fun map(albumResponse: ApiAlbumResponse): List<DeezerAlbum> {
        val albums = albumResponse.data

        return albums.map {
            DeezerAlbum(
                it.id,
                it.title,
                it.coverMedium,
                it.nbTracks,
                it.tracklist,
                it.artist.id,
                it.artist.name
            )
        }
    }
}