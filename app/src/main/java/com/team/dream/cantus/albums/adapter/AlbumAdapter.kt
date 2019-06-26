package com.team.dream.cantus.albums.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.model.DeezerAlbum

class AlbumAdapter: RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    var albumList: List<DeezerAlbum>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun getItemCount(): Int {
        return albumList?.size ?: 0
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        albumList?.let { albums ->
            val coverUrl = albums[position].cover_medium

            Picasso.get().load(coverUrl).into(holder.imvCover)
        }
    }

    class AlbumViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imvCover: ImageView = itemView.findViewById(R.id.imv_cover)
    }
}