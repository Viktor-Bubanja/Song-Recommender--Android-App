package com.example.songrecommender


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track

class TrackAdapter(val context: Context,
                   val tracks: List<Track>?,
                   val clickListener: (Track?) -> Unit): RecyclerView.Adapter<TrackViewHolder>() {

    override fun getItemCount(): Int = tracks?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TrackViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.track_item, parent, false)
        val holder = TrackViewHolder(view)
        view.setOnClickListener {clickListener(tracks?.get(holder.adapterPosition))}
        return holder
    }

    override fun onBindViewHolder(holder: TrackViewHolder, i: Int) {
        holder.trackText.text = tracks?.get(i)?.name
        holder.artistText.text = tracks?.get(i)?.artists?.joinToString(", ") { it.name }
    }
}