package com.example.songrecommender

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse


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
        initialiseUI()

        if (!isSpotifyInstalled(packageManager)) {
            startActivity(Intent(this, InstallSpotifyActivity::class.java))
        }

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                genreSpinner.setSelection(getInt(GENRE_INDEX))
                emotionSeekBar.progress = getInt(EMOTION)
                danceSeekBar.progress = getInt(DANCEABILITY)
                acousticSeekBar.progress = getInt(ACOUSTIC)
            }
            searchButton.visibility = View.VISIBLE
        }

        if (!intent.getBooleanExtra("loggedIn", false)) {
            val request = SpotifyService.getAuthenticationRequest(AuthorizationResponse.Type.TOKEN)
            AuthorizationClient.openLoginActivity(
                this,
                SpotifyConstants.AUTH_TOKEN_REQUEST_CODE,
                request
            )
        } else {
            searchButton.visibility = View.VISIBLE
        }

        searchButton.setOnClickListener {
            searchingToast?.show()
            val searchAttributes = SearchAttributesWrapper(
                genreSpinner.selectedItem.toString(),
                emotionSeekBar.progress,
                danceSeekBar.progress,
                acousticSeekBar.progress,
                this.resources.getInteger(R.integer.maximum)
            )
            SongRecommender(this, searchAttributes, SpotifyService.api).execute()
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
        outState.run {
            putInt(GENRE_INDEX, genreSpinner.selectedItemPosition)
            putInt(EMOTION, emotionSeekBar.progress)
            putInt(DANCEABILITY, danceSeekBar.progress)
            putInt(ACOUSTIC, acousticSeekBar.progress)
            putBoolean(LOGGED_IN, loggedIn)
        }

        super.onSaveInstanceState(outState)
    }

    private fun createGenreDropdown() {
        genreSpinner = findViewById(R.id.genreSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.genres,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genreSpinner.adapter = adapter
        }
    }


    private fun initialiseUI() {
        emotionSeekBar = findViewById(R.id.emotionSeekBar)
        danceSeekBar = findViewById(R.id.danceSeekBar)
        acousticSeekBar = findViewById(R.id.acousticSeekBar)
        searchButton = findViewById(R.id.searchButton)
        searchButton.visibility = View.GONE

        searchingToast = Toast.makeText(
            this@MainActivity,
            getString(R.string.searching),
            Toast.LENGTH_SHORT)

        failedToast = Toast.makeText(
            this@MainActivity,
            getString(R.string.failed),
            Toast.LENGTH_LONG)
        createGenreDropdown()
    }

    companion object {
        const val GENRE_INDEX = "genre"
        const val EMOTION = "emotion"
        const val DANCEABILITY = "danceability"
        const val ACOUSTIC = "acoustic"
        const val LOGGED_IN = "loggedIn"
    }
}
