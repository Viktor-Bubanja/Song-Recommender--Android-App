package com.example.songrecommender

import android.util.Log
import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.endpoints.client.LibraryType
import com.adamratzman.spotify.models.CurrentlyPlayingObject
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
                "user-library-modify",
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

    fun play(songsToPlay: List<String>, deviceId: String) {
        api?.player?.startPlayback(
            tracksToPlay = songsToPlay,
            deviceId = deviceId)?.complete()
    }

    fun saveTrack() {
        val songId = api?.player?.getCurrentlyPlaying()?.complete()?.track?.id
        Log.d("AAA", songId.toString())
        api?.library?.add(LibraryType.TRACK, songId!!)?.complete()
    }
}