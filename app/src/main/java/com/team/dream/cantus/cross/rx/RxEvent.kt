package com.team.dream.cantus.cross.rx

import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack

class RxEvent {

//     ex for here :
//     data class EventAddPerson(val personName: String)
    data class EventTrackSelection(val album: DeezerAlbum, val tracks: List<DeezerTrack>, val selectedTrack: DeezerTrack)

//     publish example :
//     RxBus.publish(RxEvent.EventAddPerson(etPersonName.text.toString()))

//    Example on activity
//    class Activity {
//
//        private lateinit var personDisposable: Disposable
//
//        onCreate() {
//            personDisposable = RxBus.listen(RxEvent.EventAddPerson::class.java).subscribe {
//                adapter.addPerson(person = it.personName)
//            }
//        }
//
//        onDestroy() {
//            if (!personDisposable.isDisposed) personDisposable.dispose()
//        }
//    }
}