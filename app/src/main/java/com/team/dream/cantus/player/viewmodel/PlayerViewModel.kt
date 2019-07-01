package com.team.dream.cantus.player.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.cross.rx.RxBus
import com.team.dream.cantus.cross.rx.RxEvent

class PlayerViewModel(app: Application) : AndroidViewModel(app) {

    private val albumMutableLiveData = MutableLiveData<DeezerAlbum>()
    private val trackMutableLiveData = MutableLiveData<DeezerTrack>()
    private val isPlayingMutableLiveData = MutableLiveData<Boolean>()
    private val toastMutableLiveData = MutableLiveData<Int>()
    val albumLiveData: LiveData<DeezerAlbum> = albumMutableLiveData
    val trackLiveData: LiveData<DeezerTrack> = trackMutableLiveData
    val isPlayingLiveData: LiveData<Boolean> = isPlayingMutableLiveData
    val toastLiveData: LiveData<Int> = toastMutableLiveData

    private val onPlayPauseDisposable = RxBus.listen(RxEvent.EventOnPlayPause::class.java).subscribe {
        isPlayingMutableLiveData.value = it.isPlaying
    }
    private val onTrackUpdatedDisposable = RxBus.listen(RxEvent.EventOnTrackUpdated::class.java).subscribe {
        trackMutableLiveData.value = it.track
        albumMutableLiveData.value = it.album
    }
    private val onPlayErrorDisposable = RxBus.listen(RxEvent.EventOnPlayError::class.java).subscribe {
        toastMutableLiveData.value = it.message
    }

    private var currentAlbumTracks: List<DeezerTrack>? = null

    init {
    }

    private fun updateTrack(track: DeezerTrack) {
        trackMutableLiveData.value = track
    }

    fun onClickPlayPause() {

    }

    fun onClickPrevious() {
    }

    fun onClickNext() {
    }

    fun onDestroy() {
        onPlayErrorDisposable.dispose()
        onTrackUpdatedDisposable.dispose()
        onPlayPauseDisposable.dispose()
    }
}