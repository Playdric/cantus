package com.team.dream.cantus.albums.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.dream.cantus.cross.com.DeezerProvider
import com.team.dream.cantus.cross.model.DeezerAlbum

class AlbumViewModel : ViewModel() {

    private val TAG = "AlbumViewModel"

    private val albumsMutableLiveData = MutableLiveData<List<DeezerAlbum>>()
    val albumsLiveData: LiveData<List<DeezerAlbum>> = albumsMutableLiveData

    fun getAlbums(index: Int) {
        DeezerProvider.getAlbums(index, object : DeezerProvider.Listener<List<DeezerAlbum>> {
            override fun onSuccess(data: List<DeezerAlbum>) {
                albumsMutableLiveData.value = data
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
            }

        })
    }

}