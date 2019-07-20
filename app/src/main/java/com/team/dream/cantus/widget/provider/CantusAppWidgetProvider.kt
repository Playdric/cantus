package com.team.dream.cantus.widget.provider

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.team.dream.cantus.R
import com.team.dream.cantus.player.service.PlayerService


class CantusAppWidgetProvider : AppWidgetProvider() {

    companion object {
        const val WIDGET_NEXT = "com.team.dream.cantus.WIDGET_NEXT"
        const val WIDGET_PLAY = "com.team.dream.cantus.WIDGET_PLAY"
        const val WIDGET_PREVIOUS = "com.team.dream.cantus.WIDGET_PREVIOUS"

        const val INTENT_EXTRA_IS_PLAYING = "INTENT_EXTRA_IS_PLAYING"
        const val INTENT_EXTRA_IMG_ALBUM = "INTENT_EXTRA_IMG_ALBUM"
        const val INTENT_EXTRA_ALBUM_TITLE = "INTENT_EXTRA_ALBUM_TITLE"
        const val INTENT_EXTRA_TRACK_TITLE = "INTENT_EXTRA_TRACK_TITLE"

        const val TAG = "CantusAppWidgetProvider"
    }

    private var isPlaying = false
    private var imgAlbum = ""
    private var albumTitle = ""
    private var trackTitle = ""

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { appWidgetId ->
            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.cantus_appwidget
            ).apply {
                setOnClickPendingIntent(R.id.appwidget_btn_previous, getPendingIntent(context, WIDGET_PREVIOUS))
                setOnClickPendingIntent(R.id.appwidget_btn_play_stop, getPendingIntent(context, WIDGET_PLAY))
                setOnClickPendingIntent(R.id.appwidget_btn_next, getPendingIntent(context, WIDGET_NEXT))

                if (trackTitle.isNotEmpty()) {
                    setTextViewText(R.id.appwidget_txv_title, trackTitle)
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_txv_title)
                }
                if (albumTitle.isNotEmpty()) {
                    setTextViewText(R.id.appwidget_txv_album, albumTitle)
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_txv_album)
                }
                if (imgAlbum.isNotEmpty()) {
                    Picasso.get().load(imgAlbum).into(this, R.id.appwidget_imv_album_cover, appWidgetIds)
                }
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun getPendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, CantusAppWidgetProvider::class.java)
        intent.action = action

        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("COUCOU", "received:${intent.action}")
        when (intent.action) {
            WIDGET_NEXT -> {
                Log.i(TAG, "onClick next")
                sendIntentToPlayer(context, PlayerService.ACTION_NEXT)
            }
            WIDGET_PLAY -> {
                Log.d(TAG, "onClick play/pause")
                sendIntentToPlayer(context, PlayerService.ACTION_PLAY_PAUSE)
            }
            WIDGET_PREVIOUS -> {
                Log.d(TAG, "onClick previous")
                sendIntentToPlayer(context, PlayerService.ACTION_PREVIOUS)
            }
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val extras = intent.extras
                extras?.let {
                    if (it.containsKey(INTENT_EXTRA_IS_PLAYING)) {
                        isPlaying = it.getBoolean(INTENT_EXTRA_IS_PLAYING)
                    }
                    if (it.containsKey(INTENT_EXTRA_IMG_ALBUM)) {
                        imgAlbum = it.getString(INTENT_EXTRA_IMG_ALBUM)!!
                    }
                    if (it.containsKey(INTENT_EXTRA_ALBUM_TITLE)) {
                        albumTitle = it.getString(INTENT_EXTRA_ALBUM_TITLE)!!
                    }
                    if (it.containsKey(INTENT_EXTRA_TRACK_TITLE)) {
                        trackTitle = it.getString(INTENT_EXTRA_TRACK_TITLE)!!
                    }
                    val appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS)
                    appWidgetIds?.let { this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds) }
                    return
                }
            }
        }
        super.onReceive(context, intent)
    }

    private fun sendIntentToPlayer(context: Context, action: String) {
        val intent = Intent(context, PlayerService::class.java)
        intent.action = action
        ContextCompat.startForegroundService(context, intent)
    }
}