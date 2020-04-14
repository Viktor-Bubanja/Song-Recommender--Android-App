package com.example.songrecommender

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.AuthorizationClient


class MainActivity : AppCompatActivity() {

    private var loggedIn = false
    private var searchingToast: Toast? = null
    private var failedToast: Toast? = null

    private lateinit var genreSpinner: Spinner
    private lateinit var emotionSeekBar: SeekBar
    private lateinit var danceSeekBar: SeekBar
    private lateinit var acousticSeekBar: SeekBar

    private lateinit var searchButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        genreSpinner = findViewById(R.id.genreSpinner)
        emotionSeekBar = findViewById(R.id.emotionSeekBar)
        danceSeekBar = findViewById(R.id.danceSeekBar)
        acousticSeekBar = findViewById(R.id.acousticSeekBar)
        searchButton = findViewById(R.id.searchButton)
        searchButton.visibility = View.GONE

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                genreSpinner.setSelection(getInt(GENRE_INDEX))
                emotionSeekBar.progress = getInt(EMOTION)
                danceSeekBar.progress = getInt(DANCEABILITY)
                acousticSeekBar.progress = getInt(ACOUSTIC)
            }
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.genres,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            genreSpinner.adapter = adapter
        }

        if (!intent.getBooleanExtra("loggedIn", false)) {
            val request = SpotifyService.getAuthenticationRequest(AuthorizationResponse.Type.TOKEN)
            AuthorizationClient.openLoginActivity(
                this,
                SpotifyConstants.AUTH_TOKEN_REQUEST_CODE,
                request
            )
        }

        searchingToast = Toast.makeText(
            this@MainActivity,
            getString(R.string.searching),
            Toast.LENGTH_SHORT)

        failedToast = Toast.makeText(
            this@MainActivity,
            getString(R.string.failed),
            Toast.LENGTH_LONG)

        searchButton.setOnClickListener {
            try {
                searchingToast?.show()
                val searchAttributes = SearchAttributesWrapper(
                    genreSpinner.selectedItem.toString(),
                    emotionSeekBar.progress,
                    danceSeekBar.progress,
                    acousticSeekBar.progress,
                    this.resources.getInteger(R.integer.maximum)
                )
                SongRecommender(this, searchAttributes).execute(SpotifyService.api)
            } catch (e: Exception) {
                Log.e("MainActivity", e.message)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (SpotifyConstants.AUTH_TOKEN_REQUEST_CODE == requestCode) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            val accessToken: String? = response.accessToken
            SpotifyService.setApi(accessToken)
            loggedIn = true
            searchButton.visibility = View.VISIBLE
        }
    }

    fun showPlayer() {
        val intent = Intent(this, PlayerActivity::class.java)
        searchingToast?.cancel()
        startActivity(intent)
    }

    fun showFailedToast() {
        failedToast?.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Save the user's current game state
        outState.run {
            putInt(GENRE_INDEX, genreSpinner.selectedItemPosition)
            putInt(EMOTION, emotionSeekBar.progress)
            putInt(DANCEABILITY, danceSeekBar.progress)
            putInt(ACOUSTIC, acousticSeekBar.progress)
            putBoolean(LOGGED_IN, loggedIn)
        }

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val GENRE_INDEX = "genre"
        const val EMOTION = "emotion"
        const val DANCEABILITY = "danceability"
        const val ACOUSTIC = "acoustic"
        const val LOGGED_IN = "loggedIn"
    }
}
