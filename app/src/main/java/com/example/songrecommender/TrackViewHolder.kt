package com.example.songrecommender

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val trackText: TextView = view.findViewById(R.id.trackText)

    var isActive: Boolean = false
        set(value) {
            field = value
            itemView.setBackgroundColor(if (field) Color.LTGRAY else Color.TRANSPARENT)
        }
}