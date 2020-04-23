package com.example.songrecommender

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity() : Activity() {

    private lateinit var playAnimation: AnimationDrawable
    private lateinit var pauseAnimation: AnimationDrawable
    private lateinit var playButton: ImageButton
    private lateinit var pauseButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton: ImageButton = findViewById(R.id.backButton)
        val saveButton: ImageButton = findViewById(R.id.saveButton)
        val removeButton: ImageButton = findViewById(R.id.removeButton)
        val songText: TextView = findViewById(R.id.songText)
        val artistText: TextView = findViewById(R.id.artistText)
        val queueButton: ImageButton = findViewById(R.id.queueButton)
        removeButton.visibility = View.INVISIBLE

        songText.text = SpotifyService.getCurrentlyPlayingTrack()?.name
        artistText.text = SpotifyService.getCurrentlyPlayingTrack()?.artists?.joinToString(", ") { it.name }

        playButton = findViewById<ImageButton>(R.id.playButton).apply {
            setBackgroundResource(R.drawable.play_animation)
            playAnimation = background as AnimationDrawable
        }


        pauseButton = findViewById<ImageButton>(R.id.pauseButton).apply {
            setBackgroundResource(R.drawable.pause_animation)
            pauseAnimation = background as AnimationDrawable
        }

        showPauseButton()
        if (!SpotifyService.isPlaying()) {
            ResumeTrack(SpotifyService.api).execute()
        }

        playButton.setOnClickListener {
            if (SpotifyService.isPlaying()) {
                showPauseButton()
                pauseAnimation.start()
                PauseTrack(SpotifyService.api).execute()
            } else {
                playAnimation.start()
                ResumeTrack(SpotifyService.api).execute()
            }
        }

        pauseButton.setOnClickListener {
            if (SpotifyService.isPlaying()) {
                pauseAnimation.start()
                PauseTrack(SpotifyService.api).execute()
            } else {
                showPlayButton()
                playAnimation.start()
                ResumeTrack(SpotifyService.api).execute()
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("loggedIn", true)
            startActivity(intent)
        }

        queueButton.setOnClickListener {
            startActivity(Intent(this, QueueActivity::class.java))
        }

        saveButton.setOnClickListener {
            SpotifyService.saveCurrentTrack()
            saveButton.visibility = View.INVISIBLE
            removeButton.visibility = View.VISIBLE
            Toast.makeText(this, getString(R.string.track_saved), Toast.LENGTH_SHORT)
                .show()
        }

        removeButton.setOnClickListener {
            SpotifyService.removeCurrentTrack()
            removeButton.visibility = View.INVISIBLE
            saveButton.visibility = View.VISIBLE
            Toast.makeText(this, getString(R.string.track_removed), Toast.LENGTH_SHORT)
                .show()
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
