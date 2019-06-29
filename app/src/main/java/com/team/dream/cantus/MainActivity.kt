package com.team.dream.cantus

import android.media.MediaPlayer
import android.media.session.MediaSession
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.player.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var viewModel = PlayerViewModel()
    private var mediaPlayer = MediaPlayer()

    private lateinit var mediaSession: MediaSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initObservers()
        setClickListeners()
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }

    private fun initObservers() {
        viewModel.albumLiveData.observe(this, Observer {
            updateAlbum(it)
        })
        viewModel.trackLiveData.observe(this, Observer {
            updateTrack(it)
        })
    }

    private fun updateTrack(track: DeezerTrack) {
        txv_track_title.text = track.titleShort
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(track.preview)
            mediaPlayer.prepare()
        } catch (err: Exception) {
            err.printStackTrace()
            Toast.makeText(this, R.string.error_reading_track, Toast.LENGTH_SHORT).show()
        }

        handleMediaPlayerView(mediaPlayer.isPlaying)
    }

    private fun updateAlbum(album: DeezerAlbum) {
        Picasso
                .get()
                .load(album.cover_medium)
                .placeholder(R.drawable.ic_album_placeholder)
                .into(imv_album)

        txv_track_artist.text = album.artistName
    }

    private fun setClickListeners() {
        btn_play_stop.setOnClickListener {
            handleMediaPlayerView(mediaPlayer.isPlaying)
        }
        btn_previous.setOnClickListener {
            viewModel.getPrevious()
        }
        btn_next.setOnClickListener {
            viewModel.getNext()
        }
    }

    private fun handleMediaPlayerView(playing: Boolean) {
        if (playing) {
            btn_play_stop.setImageResource(R.drawable.ic_play)
            mediaPlayer.pause()
            return
        }

        btn_play_stop.setImageResource(R.drawable.ic_pause)
        mediaPlayer.start()
    }
}
