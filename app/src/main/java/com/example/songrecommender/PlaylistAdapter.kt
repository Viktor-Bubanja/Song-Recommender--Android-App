package com.example.songrecommender


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.SimplePlaylist

class PlaylistAdapter(val context: Context,
                      val playlists: List<SimplePlaylist>): RecyclerView.Adapter<PlaylistViewHolder>() {
//                      val clickListener: (SimplePlaylist) -> Unit): RecyclerView.Adapter<PlaylistViewHolder>() {

    private var selectedIndex = RecyclerView.NO_POSITION

    override fun getItemCount(): Int = playlists.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.playlist_item, parent, false)
        val holder = PlaylistViewHolder(view)
        view.setOnClickListener {
//            clickListener(playlists[holder.adapterPosition])
            val oldSelectedIndex = selectedIndex
            selectedIndex = holder.adapterPosition
            notifyItemChanged(selectedIndex)
            notifyItemChanged(oldSelectedIndex)
        }
        return holder
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, i: Int) {
        holder.playlistText.text = playlists[i].name
        holder.isActive = selectedIndex == i
    }
}