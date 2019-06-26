package com.team.dream.cantus.tracklist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.dream.cantus.cross.com.DeezerProvider
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack

class TracklistViewModel: ViewModel() {

    private val TAG = "AlbumViewModel"

    private val tracklistMutableLiveData = MutableLiveData<List<DeezerTrack>>()
    val tracklistLiveData : LiveData<List<DeezerTrack>> = tracklistMutableLiveData

    fun getTracklist(tracklistId: Int) {
        DeezerProvider.getTracks(tracklistId, object: DeezerProvider.Listener<List<DeezerTrack>> {
            override fun onSuccess(data: List<DeezerTrack>) {
                tracklistMutableLiveData.value = data
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
                tracklistMutableLiveData.value = listOf(DeezerTrack(0, "Error no data", "Error no data", "Error no data"))
            }

        })
    }
}