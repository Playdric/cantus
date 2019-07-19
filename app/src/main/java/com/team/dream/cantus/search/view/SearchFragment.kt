package com.team.dream.cantus.tracklist.view

import android.graphics.drawable.BitmapDrawable
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
import com.airbnb.lottie.LottieAnimationView
import com.squareup.picasso.Picasso
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.model.DeezerAlbum
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.cross.rx.RxBus
import com.team.dream.cantus.cross.rx.RxEvent
import com.team.dream.cantus.tracklist.adapter.SearchAdapter
import com.team.dream.cantus.tracklist.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var rcvTracklist: RecyclerView
    private lateinit var imvAlbumCover: AppCompatImageView
    private lateinit var txvAlbumName: AppCompatTextView
    private lateinit var txvArtist: AppCompatTextView
    private lateinit var txvNbTrack: AppCompatTextView
    private lateinit var lottieAnimationTrumpet: LottieAnimationView
    private lateinit var toolbarBackground: View
    private lateinit var motionLayout: MotionLayout
    private lateinit var adapter: SearchAdapter

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
        }
        initRecyclerView()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        viewModel.tracklistLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                updateAlbumList(it)
            } else {
            }
        })
    }

    private fun initRecyclerView() {
        adapter = SearchAdapter()
        rcvTracklist.adapter = adapter
        rcvTracklist.layoutManager = LinearLayoutManager(context)
        adapter.setListener(object : SearchAdapter.ClickListener {
            override fun onClick(track: DeezerTrack) {
                adapter.tracks?.run {
                    RxBus.publish(RxEvent.EventTrackSelection(DeezerAlbum(1, "coucou", "", 3), this, track))
                }
            }

        })
    }

    private fun updateAlbumList(tracks: List<DeezerTrack>) {
        adapter.tracks = tracks
    }
}