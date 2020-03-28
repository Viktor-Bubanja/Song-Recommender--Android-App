package com.example.songrecommender

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity() : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val playButton: ImageButton = findViewById(R.id.playButton)
        val pauseButton: ImageButton = findViewById(R.id.pauseButton)
        val backButton: Button = findViewById(R.id.backButton)
        val saveButton: Button = findViewById(R.id.saveButton)

        showPauseButton()

        playButton.setOnClickListener {
            SpotifyService.resume()
            showPauseButton()
        }

        pauseButton.setOnClickListener {
            SpotifyService.pause()
            showPlayButton()
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("loggedIn", true)
            startActivity(intent)
        }

        saveButton.setOnClickListener {
            val intent = Intent(this, PlaylistActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showPlayButton() {
        playButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
    }

    private fun showPauseButton() {
        playButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
    }
}
