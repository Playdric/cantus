package com.team.dream.cantus.cross.rx

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

object RxBus {
    private val publisher = PublishSubject.create<Any>()
    private val behaviorPlayPause = BehaviorSubject.create<Any>()
    private val behaviorTrackUpdate = BehaviorSubject.create<Any>()
    private val behaviorStopPlaying = BehaviorSubject.create<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    fun publishPlayPause(event: Any) {
        behaviorPlayPause.onNext(event)
    }

    fun publishTrackUpdate(event: Any) {
        behaviorTrackUpdate.onNext(event)
    }

    fun publishStopPlaying(event: Any) {
        behaviorStopPlaying.onNext(event)
    }


    fun <T> listenToPublishSubject(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)


    fun <T> listenToPlayPause(eventType: Class<T>): Observable<T> = behaviorPlayPause.ofType(eventType)

    fun <T> listenToTrackUpdate(eventType: Class<T>): Observable<T> = behaviorTrackUpdate.ofType(eventType)

    fun <T> listenToStopPlaying(eventType: Class<T>): Observable<T> {
        return behaviorStopPlaying.ofType(eventType)
    }

}