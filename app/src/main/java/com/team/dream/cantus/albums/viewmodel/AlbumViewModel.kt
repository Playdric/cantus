package com.team.dream.cantus.albums.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.dream.cantus.cross.com.DeezerProvider
import com.team.dream.cantus.cross.model.DeezerAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumViewModel: ViewModel() {

    private val TAG = "AlbumViewModel"

    private val albumsMutableLiveData = MutableLiveData<List<DeezerAlbum>>()
    val albumsLiveData :LiveData<List<DeezerAlbum>> = albumsMutableLiveData

    fun getAlbums() {
        DeezerProvider.getAlbums(object: DeezerProvider.Listener<List<DeezerAlbum>> {
            override fun onSuccess(data: List<DeezerAlbum>) {
                albumsMutableLiveData.value = data
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
            }

        })
    }

}