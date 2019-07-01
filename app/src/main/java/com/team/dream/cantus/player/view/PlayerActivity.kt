package com.team.dream.cantus.player.view

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.cross.rx.RxBus
import com.team.dream.cantus.cross.rx.RxEvent
import com.team.dream.cantus.player.service.PlayerService
import com.team.dream.cantus.player.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.activity_main.*


class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel
    private var mediaPlayer = MediaPlayer()


    private val onTrackSelectionDisposable = RxBus.listen(RxEvent.EventTrackSelection::class.java).subscribe {
        val intent = Intent(this, PlayerService::class.java)
        intent.action = PlayerService.ACTION_SET_TRACKLIST
        val bundle = Bundle()
        bundle.putParcelableArrayList(PlayerService.BUNDLE_KEY_TRACKLIST, ArrayList(it.tracks))
        bundle.putParcelable(PlayerService.BUNDLE_KEY_CURR_TRACK, it.selectedTrack)
        bundle.putParcelable(PlayerService.BUNDLE_KEY_ALBUM, it.album)
        intent.putExtra(PlayerService.BUNDLE_NAME, bundle)
        ContextCompat.startForegroundService(this, intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = PlayerViewModel(application)

        initObservers()
        setClickListeners()
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        onTrackSelectionDisposable.dispose()
        val intent = Intent(this, PlayerService::class.java)
        stopService(intent)
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
        viewModel.toastLiveData.observe(this, Observer {
            onChangeToast(it)
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

    private fun onChangeToast(value: Int) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
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
        sendIntent(PlayerService.ACTION_PLAY_PAUSE)
    }

    private fun onClickPrevious() {
        sendIntent(PlayerService.ACTION_PREVIOUS)
    }

    private fun onClickNext() {
        sendIntent(PlayerService.ACTION_NEXT)
    }

    private fun sendIntent(action: String) {
        val intent = Intent(this, PlayerService::class.java)
        intent.action = action
        ContextCompat.startForegroundService(this, intent)
    }
}
