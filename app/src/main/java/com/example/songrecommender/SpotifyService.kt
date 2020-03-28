package com.example.songrecommender

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyClientApi
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


object SpotifyService {

    var api: SpotifyClientApi ?= null

    fun getAuthenticationRequest(type: AuthorizationResponse.Type): AuthorizationRequest {
        return AuthorizationRequest.Builder(SpotifyConstants.CLIENT_ID, type, SpotifyConstants.REDIRECT_URI)
            .setShowDialog(false)
            .setScopes(arrayOf(
                "playlist-read-private",
                "playlist-modify-public",
                "playlist-modify-private",
                "user-read-currently-playing",
                "user-modify-playback-state",
                "user-read-playback-state",
                "app-remote-control",
                "streaming"

            ))
            .build()
    }

    fun setApi(accessToken: String?) {
        this.api = SpotifyApi.spotifyClientApi(
            SpotifyConstants.CLIENT_ID,
            SpotifyConstants.CLIENT_SECRET,
            SpotifyConstants.REDIRECT_URI
        ) {
            authorization {
                tokenString = accessToken
            }
        }.build()
    }

    fun pause() {
        api?.player?.pause()?.complete()
    }

    fun resume() {
        api?.player?.resume()?.complete()
    }
}