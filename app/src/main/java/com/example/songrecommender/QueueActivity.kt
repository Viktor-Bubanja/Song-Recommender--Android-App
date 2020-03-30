package com.example.songrecommender

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track

class QueueActivity: Activity() {

    private lateinit var trackPicker: RecyclerView

    var tracks: List<Track> = listOf()
        set(value) {
            field = value
            trackPicker.adapter = TrackAdapter(this, field)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue)
        trackPicker = findViewById(R.id.trackPicker)
        val layoutManager = LinearLayoutManager(this)
        trackPicker.layoutManager = layoutManager
    }
}