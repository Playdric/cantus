package com.team.dream.cantus.player.service

import android.app.*
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.IBinder
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media.MediaSessionManager
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.cross.rx.RxBus
import com.team.dream.cantus.cross.rx.RxEvent
import com.team.dream.cantus.player.view.PlayerActivity
import com.team.dream.cantus.widget.provider.CantusAppWidgetProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlayerService : Service() {

    companion object {
        const val ACTION_PLAY_PAUSE = "ACTION_PLAY_PAUSE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_CLOSE = "ACTION_CLOSE"
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

    private var bitmapAlbum: Bitmap? = null

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaManager: MediaSessionManager
    private var mediaController: MediaControllerCompat? = null

    private lateinit var tracklist: List<DeezerTrack>
    private lateinit var album: DeezerAlbum
    private lateinit var currentTrack: DeezerTrack

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand()")
        RxBus.publishStopPlaying(RxEvent.EventOnStopPlaying(false))
        sendIntentToWidget(isPlaying = false)
        if (mediaController == null) {
            initMedia()
            createNotificationChannel()
        }

        intent?.let {
            handleintentAction(it)
        }

        return super.onStartCommand(intent, flags, startId)
    }

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

    private fun handleintentAction(intent: Intent) {

        intent.action?.let {
            when (it) {
                ACTION_PLAY_PAUSE -> mediaController!!.transportControls.playPause()
                ACTION_NEXT -> mediaController!!.transportControls.skipToNext()
                ACTION_PREVIOUS -> mediaController!!.transportControls.skipToPrevious()
                ACTION_STOP -> mediaController!!.transportControls.stop()
                ACTION_SET_TRACKLIST -> setTrackList(intent)
                ACTION_CLOSE -> stopSelf()
            }
        }
    }

    private fun setTrackList(intent: Intent) {
        val bundle = intent.getBundleExtra(BUNDLE_NAME)
        currentTrack = bundle.getParcelable(BUNDLE_KEY_CURR_TRACK)
        album = bundle.getParcelable(BUNDLE_KEY_ALBUM)
        tracklist = bundle.getParcelableArrayList(BUNDLE_KEY_TRACKLIST)
        val notification =
            buildNotification(generateAction(com.team.dream.cantus.R.drawable.ic_pause, "pause", ACTION_PLAY_PAUSE))
        startForeground(1, notification)
        updateTrack(currentTrack)
        downloadBitmap()
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
            override fun onPlay() {
                super.onPlay()
                mediaPlayer.start()
                RxBus.publishPlayPause(RxEvent.EventOnPlayPause(isPlaying = true))
                sendIntentToWidget(isPlaying = true)
                buildNotification(generateAction(com.team.dream.cantus.R.drawable.ic_pause, "pause", ACTION_PLAY_PAUSE))
                Log.i(TAG, "onPlay() called")
            }

            override fun onPause() {
                super.onPause()
                mediaPlayer.pause()
                RxBus.publishPlayPause(RxEvent.EventOnPlayPause(isPlaying = false))
                sendIntentToWidget(isPlaying = false)
                buildNotification(generateAction(com.team.dream.cantus.R.drawable.ic_play, "play", ACTION_PLAY_PAUSE))
                Log.i(TAG, "onPause() called")
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
                getNext()
                buildNotification(generateAction(com.team.dream.cantus.R.drawable.ic_pause, "pause", ACTION_PLAY_PAUSE))
                Log.i(TAG, "onSkipToNext() called")
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
                getPrevious()
                buildNotification(generateAction(com.team.dream.cantus.R.drawable.ic_pause, "pause", ACTION_PLAY_PAUSE))
                Log.i(TAG, "onSkipToPrevious() called")
            }
        })

        mediaManager = MediaSessionManager.getSessionManager(applicationContext)
    }

    private fun buildNotification(action: NotificationCompat.Action): Notification {

        val contentIntent = Intent(applicationContext, PlayerActivity::class.java)
        contentIntent.action = Intent.ACTION_MAIN
        contentIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val contentPendingIntent = PendingIntent.getActivity(applicationContext, 2, contentIntent, 0)

        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)
            .setShowActionsInCompactView(0, 1, 2)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(com.team.dream.cantus.R.drawable.ic_notification)
            .setContentTitle(currentTrack.title)
            .setContentText(album.title)
            .setStyle(mediaStyle)
            .setContentIntent(contentPendingIntent)
            .addAction(generateAction(com.team.dream.cantus.R.drawable.ic_previous, "previous", ACTION_PREVIOUS))
            .addAction(action)
            .addAction(generateAction(com.team.dream.cantus.R.drawable.ic_next, "next", ACTION_NEXT))
            .addAction(generateAction(com.team.dream.cantus.R.drawable.ic_close, "close", ACTION_CLOSE))

        bitmapAlbum?.let {
            notification.setLargeIcon(it)
        }

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
        RxBus.publishTrackUpdate(RxEvent.EventOnTrackUpdated(currentTrack, album))
        sendIntentToWidget(
            imgAlbum = album.cover_medium,
            albumTitle = album.title,
            trackTitle = currentTrack.title)
        GlobalScope.launch {
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(track.preview)
                mediaPlayer.prepare()
                withContext(Dispatchers.Main) {
                    mediaController!!.transportControls.playPause()
                }
            } catch (err: Exception) {
                err.printStackTrace()
                withContext(Dispatchers.Main) {
                    RxBus.publish(RxEvent.EventOnPlayError(err, com.team.dream.cantus.R.string.error_reading_track))
                }
            }
        }

    }

    private fun downloadBitmap() {
        Picasso
            .get()
            .load(album.cover_medium)
            .into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

                override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    bitmapAlbum = bitmap
                    if (mediaPlayer.isPlaying)
                        buildNotification(
                            generateAction(
                                com.team.dream.cantus.R.drawable.ic_pause,
                                "pause",
                                ACTION_PLAY_PAUSE
                            )
                        )
                    else
                        buildNotification(
                            generateAction(
                                com.team.dream.cantus.R.drawable.ic_play,
                                "play",
                                ACTION_PLAY_PAUSE
                            )
                        )
                }

            })
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaSession.release()
        RxBus.publishStopPlaying(RxEvent.EventOnStopPlaying(true))
        sendIntentToWidget(isPlaying = false)
        super.onDestroy()
    }

    private fun MediaControllerCompat.TransportControls.playPause() {
        if (mediaPlayer.isPlaying)
            pause()
        else
            play()
    }

    private fun sendIntentToWidget(
        isPlaying: Boolean? = null,
        imgAlbum: String? = null,
        albumTitle: String? = null,
        trackTitle: String? = null
    ) {
        val intent = Intent(this, CantusAppWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application)
            .getAppWidgetIds(ComponentName(application, CantusAppWidgetProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

        isPlaying?.let {
            intent.putExtra(CantusAppWidgetProvider.INTENT_EXTRA_IS_PLAYING, it)
        }
        imgAlbum?.let {
            intent.putExtra(CantusAppWidgetProvider.INTENT_EXTRA_IMG_ALBUM, it)
        }
        albumTitle?.let {
            intent.putExtra(CantusAppWidgetProvider.INTENT_EXTRA_ALBUM_TITLE, it)
        }
        trackTitle?.let {
            intent.putExtra(CantusAppWidgetProvider.INTENT_EXTRA_TRACK_TITLE, it)
        }
        sendBroadcast(intent)
    }
}
