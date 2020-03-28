package com.example.songrecommender

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val playlistText: TextView = view.findViewById(R.id.playlistText)

    var isActive: Boolean = false
        set(value) {
            field = value
            itemView.setBackgroundColor(if (field) Color.LTGRAY else Color.TRANSPARENT)
        }
}