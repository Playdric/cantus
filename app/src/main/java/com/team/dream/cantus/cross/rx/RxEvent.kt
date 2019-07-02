package com.team.dream.cantus.cross.rx

import androidx.annotation.StringRes
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack

class RxEvent {

//     ex for here :
//     data class EventAddPerson(val personName: String)
    data class EventTrackSelection(val album: DeezerAlbum, val tracks: List<DeezerTrack>, val selectedTrack: DeezerTrack)

    data class EventOnPlayPause(val isPlaying: Boolean)

    data class EventOnTrackUpdated(val track: DeezerTrack, val album: DeezerAlbum)

    data class EventOnPlayError(val t: Throwable, @StringRes val message: Int)

    class EventOnStopPlaying()

//     publish example :
//     RxBus.publish(RxEvent.EventAddPerson(etPersonName.text.toString()))

//    Example on activity
//    class Activity {
//
//        private lateinit var personDisposable: Disposable
//
//        onCreate() {
//            personDisposable = RxBus.listenToPublishSubject(RxEvent.EventAddPerson::class.java).subscribe {
//                adapter.addPerson(person = it.personName)
//            }
//        }
//
//        onDestroy() {
//            if (!personDisposable.isDisposed) personDisposable.dispose()
//        }
//    }
}