package com.team.dream.cantus.player.view

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.player.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.activity_main.*


class PlayerActivity : AppCompatActivity() {

    private var viewModel = PlayerViewModel()
    private var mediaPlayer = MediaPlayer()

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
            onChangeAlbum(it)
        })
        viewModel.trackLiveData.observe(this, Observer {
            onChangeTrack(it)
        })
        viewModel.isPlayingLiveData.observe(this, Observer {
            onChangeIsPlaying(it)
        })
    }

    private fun onChangeTrack(track: DeezerTrack) {
        txv_track_title.text = track.titleShort
    }

    private fun onChangeAlbum(album: DeezerAlbum) {
        Picasso
                .get()
                .load(album.cover_medium)
                .placeholder(R.drawable.ic_album_placeholder)
                .into(imv_album)

        txv_track_artist.text = album.artistName
    }

    private fun onChangeIsPlaying(isPlaying: Boolean) {
        if (isPlaying) {
            btn_play_stop.setImageResource(R.drawable.ic_pause)
        } else {
            btn_play_stop.setImageResource(R.drawable.ic_play)
        }
    }

    private fun setClickListeners() {
        btn_play_stop.setOnClickListener {
            onClickPlayPause()
        }
        btn_previous.setOnClickListener {
            onClickPrevious()
        }
        btn_next.setOnClickListener {
            onClickNext()
        }
    }

    private fun onClickPlayPause() {
        viewModel.onClickPlayPause()
    }

    private fun onClickPrevious() {
        viewModel.onClickPrevious()
    }

    private fun onClickNext() {
        viewModel.onClickNext()
    }
}
