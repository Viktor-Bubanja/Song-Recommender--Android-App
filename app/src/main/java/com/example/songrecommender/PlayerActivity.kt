package com.example.songrecommender

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity() : Activity() {

    private lateinit var playAnimation: AnimationDrawable
    private lateinit var pauseAnimation: AnimationDrawable
    private lateinit var playButton: ImageButton
    private lateinit var pauseButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton: Button = findViewById(R.id.backButton)
        val saveButton: Button = findViewById(R.id.saveButton)
        val shareButton: Button = findViewById(R.id.shareButton)

        playButton = findViewById<ImageButton>(R.id.playButton).apply {
            setBackgroundResource(R.drawable.play_animation)
            playAnimation = background as AnimationDrawable
        }

        pauseButton = findViewById<ImageButton>(R.id.pauseButton).apply {
            setBackgroundResource(R.drawable.pause_animation)
            pauseAnimation = background as AnimationDrawable
        }

        showPauseButton()

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

        saveButton.setOnClickListener {
            startActivity(Intent(this, QueueActivity::class.java))
            SpotifyService.saveTrack()
        }

       shareButton.setOnClickListener {
           Log.d("AAA", SpotifyService.getCurrentlyPlayingTrack()?.externalUrls?.get(0).toString())
           SpotifyService.getCurrentlyPlayingTrack().let {
               val sharingIntent = Intent(Intent.ACTION_SEND).apply {
                   type = "text/plain"
                   putExtra(Intent.EXTRA_TEXT, it!!.externalUrls[0].url)
               }
               startActivity(Intent.createChooser(sharingIntent, "Share using"))
           }

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
