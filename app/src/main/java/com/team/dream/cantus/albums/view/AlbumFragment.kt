package com.team.dream.cantus.albums.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.dream.cantus.R
import com.team.dream.cantus.albums.adapter.AlbumAdapter
import com.team.dream.cantus.albums.viewmodel.AlbumViewModel
import com.team.dream.cantus.cross.model.DeezerAlbum

class AlbumFragment : Fragment() {

    private lateinit var viewModel: AlbumViewModel
    private lateinit var rcvAlbums: RecyclerView
    private lateinit var adapter: AlbumAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            rcvAlbums = findViewById(R.id.rcv_albums)
        }
        adapter = AlbumAdapter()
        rcvAlbums.adapter = adapter
        rcvAlbums.layoutManager = GridLayoutManager(context, 3)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AlbumViewModel::class.java)

        viewModel.albumsLiveData.observe(viewLifecycleOwner, Observer {
            updateAlbumList(it)
        })

        viewModel.getAlbums()
    }

    private fun updateAlbumList(albums: List<DeezerAlbum>) {
        adapter.albumList = albums
    }


}