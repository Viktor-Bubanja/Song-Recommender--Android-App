package com.example.songrecommender


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.Track

class TrackAdapter(val context: Context,
                   val tracks: List<Track>): RecyclerView.Adapter<TrackViewHolder>() {

    override fun getItemCount(): Int = tracks.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TrackViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.track_item, parent, false)
        val holder = TrackViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: TrackViewHolder, i: Int) {
        holder.trackText.text = tracks[i].name
    }
}