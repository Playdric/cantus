package com.team.dream.cantus.tracklist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.dream.cantus.R
import com.team.dream.cantus.tracklist.viewmodel.TracklistViewModel
import com.team.dream.cantus.cross.model.DeezerTrack
import com.team.dream.cantus.tracklist.adapter.TracklistAdapter

class TracklistFragment: Fragment() {

    private lateinit var viewModel: TracklistViewModel
    private lateinit var rcvTracklist: RecyclerView
    private lateinit var adapter: TracklistAdapter

    private val args by navArgs<TracklistFragmentArgs>()

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
        viewModel = ViewModelProviders.of(this).get(TracklistViewModel::class.java)

        viewModel.tracklistLiveData.observe(viewLifecycleOwner, Observer {
            updateAlbumList(it)
        })

        viewModel.getTracklist(args.albumId)
    }

    private fun initRecyclerView() {
        adapter = TracklistAdapter()
        rcvTracklist.adapter = adapter
        rcvTracklist.layoutManager = LinearLayoutManager(context)
        adapter.setListener(object: TracklistAdapter.ClickListener {
            override fun onClick(track: DeezerTrack) {
               TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun updateAlbumList(tracks: List<DeezerTrack>) {
        adapter.tracks = tracks
    }
}