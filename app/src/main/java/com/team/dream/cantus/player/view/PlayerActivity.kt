package com.team.dream.cantus.player.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.cross.rx.RxBus
import com.team.dream.cantus.cross.rx.RxEvent
import com.team.dream.cantus.player.service.PlayerService
import com.team.dream.cantus.player.viewmodel.PlayerViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*


class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel

    private val onTrackSelectionDisposable = RxBus.listenToPublishSubject(RxEvent.EventTrackSelection::class.java).subscribe {
        enableButtons(true)
        val intent = Intent(this, PlayerService::class.java)
        intent.action = PlayerService.ACTION_SET_TRACKLIST
        val bundle = Bundle()
        bundle.putParcelableArrayList(PlayerService.BUNDLE_KEY_TRACKLIST, ArrayList(it.tracks))
        bundle.putParcelable(PlayerService.BUNDLE_KEY_CURR_TRACK, it.selectedTrack)
        bundle.putParcelable(PlayerService.BUNDLE_KEY_ALBUM, it.album)
        intent.putExtra(PlayerService.BUNDLE_NAME, bundle)
        ContextCompat.startForegroundService(this, intent)
    }

    private lateinit var onStopPlayingDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, ViewModelProvider.AndroidViewModelFactory(application))
            .get(PlayerViewModel::class.java)
        enableButtons(false)
        setClickListeners()
    }

    override fun onResume() {
        super.onResume()
        initObservers()
        onStopPlayingDisposable = RxBus.listenToStopPlaying(RxEvent.EventOnStopPlaying::class.java).subscribe {
            if (it.isStopped) {
                enableButtons(false)
                txv_track_artist.text = ""
                txv_track_title.text = ""
                imv_album.setImageResource(R.drawable.ic_album_placeholder)
                btn_play_stop.setImageResource(R.drawable.selector_play)
            }
        }
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        onTrackSelectionDisposable.dispose()
        onStopPlayingDisposable.dispose()
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
            enableButtons(true)
            btn_play_stop.setImageResource(R.drawable.selector_pause)
        } else {
            btn_play_stop.setImageResource(R.drawable.selector_play)
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

    private fun enableButtons(status: Boolean) {
        btn_previous.isEnabled = status
        btn_play_stop.isEnabled = status
        btn_next.isEnabled = status
        btn_previous.isClickable = status
        btn_play_stop.isClickable = status
        btn_next.isClickable = status
    }

    private fun sendIntent(action: String) {
        val intent = Intent(this, PlayerService::class.java)
        intent.action = action
        ContextCompat.startForegroundService(this, intent)
    }
}
