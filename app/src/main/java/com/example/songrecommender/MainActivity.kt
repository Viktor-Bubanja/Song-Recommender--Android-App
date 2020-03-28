package com.example.songrecommender

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.AuthorizationClient



class MainActivity : AppCompatActivity() {

    private var loggedIn = false
    private var searchingToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton: Button = findViewById(R.id.searchButton)

        searchingToast = Toast.makeText(
            this@MainActivity,
            getString(R.string.searching),
            Toast.LENGTH_SHORT)

        if (!intent.getBooleanExtra("loggedIn", false)) {
            val request = SpotifyService.getAuthenticationRequest(AuthorizationResponse.Type.TOKEN)
            AuthorizationClient.openLoginActivity(
                this,
                SpotifyConstants.AUTH_TOKEN_REQUEST_CODE,
                request
            )
        }

        searchButton.setOnClickListener {
            searchingToast?.show()
            SongRecommender(this).execute(SpotifyService.api)
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
}
