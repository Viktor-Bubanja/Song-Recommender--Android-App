package com.example.songrecommender

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamratzman.spotify.models.SimplePlaylist

class PlaylistActivity: Activity() {

    private lateinit var playlistPicker: RecyclerView

    var playlists: List<SimplePlaylist> = listOf()
        set(value) {
            field = value
            playlistPicker.adapter = PlaylistAdapter(this, field)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        Log.d("AAA", "oncreate playlist activity")

        PlaylistRetriever(this).execute(SpotifyService.api)

        playlistPicker = findViewById(R.id.playlistPicker)

        Log.d("AAA", playlistPicker.toString())

        val layoutManager = LinearLayoutManager(this)
        playlistPicker.layoutManager = layoutManager
    }
}
