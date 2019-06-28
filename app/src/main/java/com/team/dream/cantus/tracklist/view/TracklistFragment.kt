package com.team.dream.cantus.tracklist.view

import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.resources.TextAppearance
import com.squareup.picasso.Picasso
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.dto.ApiAlbum
import com.team.dream.cantus.tracklist.viewmodel.TracklistViewModel
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.tracklist.adapter.TracklistAdapter

class TracklistFragment: Fragment(), MotionLayout.TransitionListener {

    private lateinit var viewModel: TracklistViewModel
    private lateinit var rcvTracklist: RecyclerView
    private lateinit var imvAlbumCover: AppCompatImageView
    private lateinit var txvAlbumName: AppCompatTextView
    private lateinit var txvArtist: AppCompatTextView
    private lateinit var txvNbTrack: AppCompatTextView
    private lateinit var adapter: TracklistAdapter

    private val args by navArgs<TracklistFragmentArgs>()

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
        }
        initRecyclerView()
        with(args.album) {
            Picasso
                .get()
                .load(cover_medium)
                .placeholder(R.drawable.ic_album_placeholder)
                .into(imvAlbumCover)
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
        adapter.setListener(object: TracklistAdapter.ClickListener {
            override fun onClick(track: DeezerTrack) {
               //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun updateAlbumList(tracks: List<DeezerTrack>) {
        adapter.tracks = tracks
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        //txvArtist.textSize = TextAppearance
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {

    }
}