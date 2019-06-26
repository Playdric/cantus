package com.team.dream.cantus.tracklist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.team.dream.cantus.R
import com.team.dream.cantus.cross.model.DeezerTrack
import kotlinx.android.synthetic.main.item_track.view.*

class TracklistAdapter: RecyclerView.Adapter<TracklistAdapter.TracklistViewHolder>(){

    private var listener: ClickListener? = null

    var tracks: List<DeezerTrack>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracklistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TracklistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks?.size ?: 0
    }

    override fun onBindViewHolder(holder: TracklistViewHolder, position: Int) {
        val track = tracks!![position]
        holder.txvTrackTitle.text = track.title
        holder.itemView.setOnClickListener { listener?.onClick(track) }
    }

    fun setListener(listener: ClickListener) {
        this.listener = listener
    }


    class TracklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txvTrackTitle: AppCompatTextView = itemView.findViewById(R.id.item_track_title)
    }

    interface ClickListener {
        fun onClick(track: DeezerTrack)
    }
}