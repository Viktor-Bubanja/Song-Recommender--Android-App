package com.example.songrecommender

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.Track

class QueueActivity: Activity() {

    private lateinit var trackPicker: RecyclerView
    var tracks: List<Track>? = SpotifyService.tracksToPlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue)

        trackPicker = findViewById(R.id.trackPicker)
        val layoutManager = LinearLayoutManager(this)
        trackPicker.layoutManager = layoutManager
        trackPicker.adapter = TrackAdapter(this, tracks) {handleTrackClick(it)}

        val decoration = DividerItemDecoration(this, layoutManager.orientation)
        trackPicker.addItemDecoration(decoration)
    }

    fun handleTrackClick(track: Track?) {
        track?.let { SpotifyService.play(listOf(track.uri.uri)) }
        startActivity(Intent(this, PlayerActivity::class.java))
    }
}