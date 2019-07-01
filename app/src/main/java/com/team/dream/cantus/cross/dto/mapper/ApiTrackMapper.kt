package com.team.dream.cantus.cross.dto.mapper

import com.team.dream.cantus.cross.dto.ApiTrack
import com.team.dream.cantus.cross.model.DeezerTrack

class ApiTrackMapper {

    fun map(apiTrack: ApiTrack): DeezerTrack{
        return apiTrack.run {

            DeezerTrack(
                id,
                title,
                titleShort,
                preview
            )
        }
    }
}