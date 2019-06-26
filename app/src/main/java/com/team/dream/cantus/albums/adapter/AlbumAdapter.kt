package com.team.dream.cantus.albums.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.model.DeezerAlbum

class AlbumAdapter: RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var listener: ClickListener? = null

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

            Picasso
                .get()
                .load(albums[position].cover_medium)
                .placeholder(R.drawable.ic_album_placeholder)
                .into(holder.imvCover)

            holder.txvAlbumName.text = albums[position].title
            holder.txvArtistName.text = albums[position].artistName
            holder.itemView.setOnClickListener{listener?.onClick(albums[position])}

        }
    }

    fun setListener(listener: ClickListener) {
        this.listener = listener
    }

    class AlbumViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imvCover: AppCompatImageView = itemView.findViewById(R.id.item_album_imv_cover)
        val txvAlbumName: AppCompatTextView = itemView.findViewById(R.id.item_album_txv_name)
        val txvArtistName: AppCompatTextView = itemView.findViewById(R.id.item_album_txv_artist)
    }

    interface ClickListener {
        fun onClick(album: DeezerAlbum)
    }
}