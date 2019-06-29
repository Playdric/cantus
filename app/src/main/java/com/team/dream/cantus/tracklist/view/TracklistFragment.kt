package com.team.dream.cantus.tracklist.view

import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.cross.rx.RxBus
import com.team.dream.cantus.cross.rx.RxEvent
import com.team.dream.cantus.tracklist.adapter.TracklistAdapter
import com.team.dream.cantus.tracklist.viewmodel.TracklistViewModel

class TracklistFragment : Fragment() {

    private lateinit var viewModel: TracklistViewModel
    private lateinit var rcvTracklist: RecyclerView
    private lateinit var imvAlbumCover: AppCompatImageView
    private lateinit var txvAlbumName: AppCompatTextView
    private lateinit var txvArtist: AppCompatTextView
    private lateinit var txvNbTrack: AppCompatTextView
    private lateinit var toolbarBackground: View
    private lateinit var motionLayout: MotionLayout
    private lateinit var adapter: TracklistAdapter

    private val args by navArgs<TracklistFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tracklist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            rcvTracklist = findViewById(R.id.rcv_tracklist)
            imvAlbumCover = findViewById(R.id.tracklist_imv_album)
            txvAlbumName = findViewById(R.id.tracklist_txv_title)
            txvArtist = findViewById(R.id.tracklist_txv_artist)
            txvNbTrack = findViewById(R.id.tracklist_txv_nb_tracks)
            toolbarBackground = findViewById(R.id.tracklist_toolbar_background)
            motionLayout = findViewById(R.id.tracklist_motion_layout)
        }
        initRecyclerView()
        with(args.album) {
            Picasso
                    .get()
                    .load(cover_medium)
                    .placeholder(R.drawable.ic_album_placeholder)
                    .into(imvAlbumCover, object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            val bmpDrawable = imvAlbumCover.drawable as BitmapDrawable
                            Palette.from(bmpDrawable.bitmap).generate {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    it?.let { palette ->
                                        val color = palette.getLightVibrantColor(resources.getColor(R.color.tracklistUncollapsedToolbar, null))
                                        toolbarBackground.setBackgroundColor(color)
                                        motionLayout.getConstraintSet(R.id.start)?.let { startConstraintSet ->
                                            startConstraintSet.setColorValue(R.id.tracklist_toolbar_background, "BackgroundColor", color)
                                        }
                                    }
                                }
                            }
                        }

                        override fun onError(e: Exception?) {
                        }

                    })
            txvAlbumName.text = title
            txvArtist.text = artistName
            txvNbTrack.text = resources.getQuantityString(R.plurals.track_number, nb_tracks, nb_tracks)

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TracklistViewModel::class.java)

        viewModel.tracklistLiveData.observe(viewLifecycleOwner, Observer {
            updateAlbumList(it)
        })

        viewModel.getTracklist(args.album.id)
    }

    private fun initRecyclerView() {
        adapter = TracklistAdapter()
        rcvTracklist.adapter = adapter
        rcvTracklist.layoutManager = LinearLayoutManager(context)
        adapter.setListener(object : TracklistAdapter.ClickListener {
            override fun onClick(track: DeezerTrack) {
                adapter.tracks?.run {
                    RxBus.publish(RxEvent.EventTrackSelection(args.album, this, track))
                }
            }

        })
    }

    private fun updateAlbumList(tracks: List<DeezerTrack>) {
        adapter.tracks = tracks
    }
}