package com.team.dream.cantus.cross.rx

class RxEvent {

    // ex for here :
    // data class EventAddPerson(val personName: String)


    // publish example :
    // RxBus.publish(RxEvent.EventAddPerson(etPersonName.text.toString()))

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