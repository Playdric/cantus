package com.team.dream.cantus.albums.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.model.DeezerAlbum

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    companion object {
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_ITEM = 1
    }

    private var listener: ClickListener? = null



    var albumList: MutableList<DeezerAlbum?>? = null
        get() {
            field?.let {
                return field!!
            }
            return mutableListOf()
        }
        set(value) {
            if (field.isNullOrEmpty()) {
                field = value
            } else {
                (field as MutableList).addAll(value as MutableList)
            }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view: View
        return when (viewType) {
            VIEW_TYPE_LOADING -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
                AlbumLoadingViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
                AlbumItemViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return albumList?.size ?: 0
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        if (holder is AlbumItemViewHolder) {

            albumList?.let { albums ->

                Picasso
                    .get()
                    .load(albums[position]!!.cover_medium)
                    .placeholder(R.drawable.ic_album_placeholder)
                    .into(holder.imvCover)

                holder.txvAlbumName.text = albums[position]!!.title
                holder.txvArtistName.text = albums[position]!!.artistName
                holder.itemView.setOnClickListener { listener?.onClick(albums[position]!!) }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (albumList!![position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    fun setListener(listener: ClickListener) {
        this.listener = listener
    }

    open class AlbumViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    class AlbumItemViewHolder(itemView: View): AlbumViewHolder(itemView) {
        val imvCover: AppCompatImageView = itemView.findViewById(R.id.item_album_imv_cover)
        val txvAlbumName: AppCompatTextView = itemView.findViewById(R.id.item_album_txv_name)
        val txvArtistName: AppCompatTextView = itemView.findViewById(R.id.item_album_txv_artist)
    }

    class AlbumLoadingViewHolder(itemView: View): AlbumViewHolder(itemView)

    interface ClickListener {
        fun onClick(album: DeezerAlbum)
    }
}