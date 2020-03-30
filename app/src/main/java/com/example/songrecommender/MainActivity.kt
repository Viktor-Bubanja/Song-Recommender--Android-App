package com.example.songrecommender

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.AuthorizationClient


class MainActivity : AppCompatActivity() {

    private var loggedIn = false

    private var searchingToast: Toast? = null

    private var failedToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val genreSpinner: Spinner = findViewById(R.id.genreSpinner)
        val emotionSeekBar: SeekBar = findViewById(R.id.emotionSeekBar)
        val danceSeekBar: SeekBar = findViewById(R.id.danceSeekBar)
        val acousticSeekBar: SeekBar = findViewById(R.id.acousticSeekBar)
        val searchButton: Button = findViewById(R.id.searchButton)

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
            searchingToast?.show()
            val searchAttributes = SearchAttributesWrapper(
                genreSpinner.selectedItem.toString(),
                emotionSeekBar.progress,
                danceSeekBar.progress,
                acousticSeekBar.progress,
                this.resources.getInteger(R.integer.maximum))
            SongRecommender(this, searchAttributes).execute(SpotifyService.api)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (SpotifyConstants.AUTH_TOKEN_REQUEST_CODE == requestCode) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            val accessToken: String? = response.accessToken
            SpotifyService.setApi(accessToken)
            loggedIn = true
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
}
