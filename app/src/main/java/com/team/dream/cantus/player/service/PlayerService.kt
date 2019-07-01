package com.team.dream.cantus.player.service

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media.MediaSessionManager
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.cross.rx.RxBus
import com.team.dream.cantus.cross.rx.RxEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlayerService() : Service() {

    companion object {
        const val ACTION_PLAY_PAUSE = "ACTION_PLAY_PAUSE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_SET_TRACKLIST = "ACTION_SET_TRACKLIST"

        const val BUNDLE_NAME = "BUNDLE_PLAYER_SERVICE"
        const val BUNDLE_KEY_TRACKLIST = "BUNDLE_KEY_TRACKLIST"
        const val BUNDLE_KEY_CURR_TRACK = "BUNDLE_KEY_CURR_TRACK"
        const val BUNDLE_KEY_ALBUM = "BUNDLE_KEY_ALBUM"

        const val SESSION_NAME = "CANTUS_MEDIA_SESSION"

        const val CHANNEL_ID = "ForegroundServiceChannel"
        const val CHANNEL_NAME = "Media Player"
        private const val TAG = "PlayerService"
    }

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaManager: MediaSessionManager
    private var mediaController: MediaControllerCompat? = null

    private lateinit var tracklist: List<DeezerTrack>
    private lateinit var album: DeezerAlbum
    private lateinit var currentTrack: DeezerTrack

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand()")
        if (mediaController == null) {
            initMedia()
            createNotificationChannel()
        }

        intent?.let {
            handleintentAction(it)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleintentAction(intent: Intent) {

        intent.action?.let {
            when (it) {
                ACTION_PLAY_PAUSE -> mediaController!!.transportControls.playPause()
                ACTION_NEXT -> mediaController!!.transportControls.skipToNext()
                ACTION_PREVIOUS -> mediaController!!.transportControls.skipToPrevious()
                ACTION_STOP -> mediaController!!.transportControls.stop()
                ACTION_SET_TRACKLIST -> setTrackList(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTrackList(intent: Intent) {
        val bundle = intent.getBundleExtra(BUNDLE_NAME)
        currentTrack = bundle.getParcelable(BUNDLE_KEY_CURR_TRACK)
        album = bundle.getParcelable(BUNDLE_KEY_ALBUM)
        tracklist = bundle.getParcelableArrayList(BUNDLE_KEY_TRACKLIST)
        val notification = buildNotification(generateAction(R.drawable.ic_pause, "pause", ACTION_PLAY_PAUSE))
        startForeground(1, notification)
        updateTrack(currentTrack)
    }

    private fun initMedia() {
        mediaPlayer = MediaPlayer()

        mediaPlayer.setOnCompletionListener {
            getNext()
        }

        mediaSession = MediaSessionCompat(applicationContext, SESSION_NAME)

        mediaController = MediaControllerCompat(applicationContext, mediaSession)

        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            val TAG = "MediaCallback"
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onPlay() {
                super.onPlay()
                mediaPlayer.start()
                RxBus.publish(RxEvent.EventOnPlayPause(isPlaying = true))
                buildNotification(generateAction(R.drawable.ic_pause, "pause", ACTION_PLAY_PAUSE))
                Log.i(TAG, "onPlay() called")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onPause() {
                super.onPause()
                mediaPlayer.pause()
                RxBus.publish(RxEvent.EventOnPlayPause(isPlaying = false))
                buildNotification(generateAction(R.drawable.ic_play, "play", ACTION_PLAY_PAUSE))
                Log.i(TAG, "onPause() called")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSkipToNext() {
                super.onSkipToNext()
                getNext()
                buildNotification(generateAction(R.drawable.ic_pause, "pause", ACTION_PLAY_PAUSE))
                Log.i(TAG, "onSkipToNext() called")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
                getPrevious()
                buildNotification(generateAction(R.drawable.ic_pause, "pause", ACTION_PLAY_PAUSE))
                Log.i(TAG, "onSkipToPrevious() called")
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onStop() {
                super.onStop()
                //TODO cancel notification
                Log.i(TAG, "onStop() called")
            }
        })

        mediaManager = MediaSessionManager.getSessionManager(applicationContext)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildNotification(action: NotificationCompat.Action): Notification {

        val deleteIntent = Intent(applicationContext, PlayerService::class.java)
        deleteIntent.action = ACTION_STOP
        val deletePendingIntent = PendingIntent.getForegroundService(applicationContext, 1, deleteIntent, 0)

        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)
            .setShowActionsInCompactView(3)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(currentTrack.title)
            .setContentText(album.title)
            .setDeleteIntent(deletePendingIntent)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .addAction(generateAction(R.drawable.ic_previous, "previous", ACTION_PREVIOUS))
            .addAction(action)
            .addAction(generateAction(R.drawable.ic_next, "next", ACTION_NEXT))

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(1, notification.build())
        return notification.build()
    }

    private fun generateAction(icon: Int, title: String, intentAction: String): NotificationCompat.Action {
        val intent = Intent(applicationContext, PlayerService::class.java)
        intent.action = intentAction
        val pendingIntent = PendingIntent.getService(applicationContext, 1, intent, 0)
        return NotificationCompat.Action.Builder(icon, title, pendingIntent).build()
    }


    private fun getNext() {
        tracklist.run {
            var index = this.indexOfFirst { it.id == currentTrack.id }
            if (index == this.lastIndex) {
                updateTrack(this[0])
                return
            }

            updateTrack(this[++index])
        }
    }

    private fun getPrevious() {
        tracklist.run {
            var index = this.indexOfFirst { it.id == currentTrack.id }
            if (index == 0) {
                updateTrack(this.last())
                return
            }

            updateTrack(this[--index])
        }
    }

    private fun updateTrack(track: DeezerTrack) {
        currentTrack = track
        RxBus.publish(RxEvent.EventOnTrackUpdated(currentTrack, album))
        GlobalScope.launch {
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(track.preview)
                mediaPlayer.prepare()
                withContext(Dispatchers.Main) {
                    mediaController!!.transportControls.playPause()
                    //mediaPlayer.start()
                }
            } catch (err: Exception) {
                err.printStackTrace()
                withContext(Dispatchers.Main) {
                    RxBus.publish(RxEvent.EventOnPlayError(err, R.string.error_reading_track))
                }
            }
        }

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mediaSession.release()
        return super.onUnbind(intent)
    }

    private fun MediaControllerCompat.TransportControls.playPause() {
        if (mediaPlayer.isPlaying)
            pause()
        else
            play()
    }
}
