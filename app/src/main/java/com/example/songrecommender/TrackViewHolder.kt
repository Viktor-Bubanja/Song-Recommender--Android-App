package com.example.songrecommender

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val trackText: TextView = view.findViewById(R.id.trackText)
    val artistText: TextView = view.findViewById(R.id.artistText)
}