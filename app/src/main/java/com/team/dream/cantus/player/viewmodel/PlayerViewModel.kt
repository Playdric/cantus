package com.team.dream.cantus.player.player.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.cross.rx.RxBus
import com.team.dream.cantus.cross.rx.RxEvent
import io.reactivex.disposables.Disposable

class PlayerViewModel: ViewModel() {
    private val albumMutableLiveData = MutableLiveData<DeezerAlbum>()
    private val trackMutableLiveData = MutableLiveData<DeezerTrack>()
    val albumLiveData: LiveData<DeezerAlbum> = albumMutableLiveData
    val trackLiveData: LiveData<DeezerTrack> = trackMutableLiveData

    private lateinit var disposable: Disposable
    private var currentAlbumTracks: List<DeezerTrack>? = null

    init {
        disposable = RxBus.listen(RxEvent.EventTrackSelection::class.java).subscribe {
            albumMutableLiveData.value = it.album
            trackMutableLiveData.value = it.selectedTrack
            currentAlbumTracks = it.tracks
        }
    }

    fun getNext() {
        currentAlbumTracks?.run {
            var index = this.indexOfFirst { it.id == trackLiveData.value?.id }
            if (index == this.lastIndex) {
                trackMutableLiveData.value = this[0]
                return
            }

            trackMutableLiveData.value = this[++index]
        }
    }

    fun getPrevious() {
        currentAlbumTracks?.run {
            var index = this.indexOfFirst { it.id == trackLiveData.value?.id }
            if (index == 0) {
                trackMutableLiveData.value = this.last()
                return
            }

            trackMutableLiveData.value = this[--index]
        }
    }
}