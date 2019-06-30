package com.team.dream.cantus.player.viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.media.session.MediaSession
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.cross.rx.RxBus
import com.team.dream.cantus.cross.rx.RxEvent
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerViewModel(app: Application) : AndroidViewModel(app) {

    private val albumMutableLiveData = MutableLiveData<DeezerAlbum>()
    private val trackMutableLiveData = MutableLiveData<DeezerTrack>()
    private val isPlayingMutableLiveData = MutableLiveData<Boolean>()
    private val toastMutableLiveData = MutableLiveData<Int>()
    val albumLiveData: LiveData<DeezerAlbum> = albumMutableLiveData
    val trackLiveData: LiveData<DeezerTrack> = trackMutableLiveData
    val isPlayingLiveData: LiveData<Boolean> = isPlayingMutableLiveData
    val toastLiveData: LiveData<Int> = toastMutableLiveData

    private var disposable: Disposable
    private var currentAlbumTracks: List<DeezerTrack>? = null
    private var mediaPlayer = MediaPlayer()

    init {
        disposable = RxBus.listen(RxEvent.EventTrackSelection::class.java).subscribe {
            albumMutableLiveData.value = it.album
            currentAlbumTracks = it.tracks
            updateTrack(it.selectedTrack)
        }

        mediaPlayer.setOnCompletionListener {
            getNext()
        }
    }

    private fun getNext() {
        currentAlbumTracks?.run {
            var index = this.indexOfFirst { it.id == trackLiveData.value?.id }
            if (index == this.lastIndex) {
                updateTrack(this[0])
                return
            }

            updateTrack(this[++index])
        }
    }

    private fun getPrevious() {
        currentAlbumTracks?.run {
            var index = this.indexOfFirst { it.id == trackLiveData.value?.id }
            if (index == 0) {
                updateTrack(this.last())
                return
            }

            updateTrack(this[--index])
        }
    }

    private fun updateTrack(track: DeezerTrack) {
        trackMutableLiveData.value = track

        GlobalScope.launch {
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(track.preview)
                mediaPlayer.prepare()
                withContext(Dispatchers.Main) {
                    play()
                }
            } catch (err: Exception) {
                err.printStackTrace()
                withContext(Dispatchers.Main) {
                    toastMutableLiveData.value = R.string.error_reading_track
                }
            }
        }

    }

    private fun play() {
        mediaPlayer.start()
        isPlayingMutableLiveData.value = true
    }

    private fun pause() {
        mediaPlayer.pause()
        isPlayingMutableLiveData.value = false
    }

    fun onClickPlayPause() {
        if (mediaPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun onClickPrevious() {
        getPrevious()
    }

    fun onClickNext() {
        getNext()
    }

    fun onDestroy() {
        disposable.dispose()
    }
}