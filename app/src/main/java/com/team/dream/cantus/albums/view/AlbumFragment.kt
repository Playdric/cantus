package com.team.dream.cantus.albums.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.team.dream.cantus.R
import com.team.dream.cantus.albums.adapter.AlbumAdapter
import com.team.dream.cantus.albums.viewmodel.AlbumViewModel
import com.team.dream.cantus.cross.model.DeezerAlbum

class AlbumFragment : Fragment() {

    private lateinit var viewModel: AlbumViewModel
    private lateinit var rcvAlbums: RecyclerView
    private lateinit var lottieAnimation: LottieAnimationView
    private var adapter = AlbumAdapter()
    private var isLoading = false
    private var currentIndex = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            rcvAlbums = findViewById(R.id.rcv_albums)
            lottieAnimation = findViewById(R.id.album_animation)
        }
        initRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AlbumViewModel::class.java)

        viewModel.albumsLiveData.observe(viewLifecycleOwner, Observer {
            lottieAnimation.visibility = View.GONE
            lottieAnimation.cancelAnimation()
            isLoading = false
            if (!it.isNullOrEmpty()) {
                updateAlbumList(it)
            }
        })

        if (currentIndex == 0) {
            isLoading = true
            viewModel.getAlbums(currentIndex)
            currentIndex += 25
        }
    }

    private fun updateAlbumList(albums: List<DeezerAlbum>) {
        adapter.albumList = albums.toMutableList()
    }

    private fun initRecyclerView() {
        rcvAlbums.adapter = adapter
        val layoutManager = GridLayoutManager(context, 3)
        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    AlbumAdapter.VIEW_TYPE_LOADING -> 3
                    else -> 1
                }
            }

        }
        rcvAlbums.layoutManager = layoutManager
        adapter.setListener(object: AlbumAdapter.ClickListener{
            override fun onClick(album: DeezerAlbum) {
                view?.also {
                    val actionDetail = AlbumFragmentDirections.actionAlbumFragmentToTracklistFragment(album)
                    Navigation.findNavController(it).navigate(actionDetail)
                }
            }

        })

        rcvAlbums.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager

                if (!isLoading) {
                    val lastItem = gridLayoutManager.findLastCompletelyVisibleItemPosition()
                    if (lastItem == adapter.albumList!!.size - 1) {
                        viewModel.getAlbums(currentIndex)
                        currentIndex += 25
                    }

                }
            }
        })
    }


}