package com.example.songrecommender

import android.util.Log
import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.endpoints.client.LibraryType
import com.adamratzman.spotify.models.CurrentlyPlayingObject
import com.adamratzman.spotify.models.Track
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


object SpotifyService {

    var api: SpotifyClientApi ?= null

    var tracksToPlay: List<Track>? = null

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

    fun isPlaying(): Boolean {
        return api?.player?.getCurrentlyPlaying()?.complete()?.isPlaying ?: false
    }

    fun play(songsToPlay: List<String>) {
        val devices = api?.player?.getDevices()?.complete()
        api?.player?.startPlayback(
            tracksToPlay = songsToPlay,
            deviceId = devices?.get(0)?.id.toString())?.complete()
    }

    fun saveTrack() {
        val songId = api?.player?.getCurrentlyPlaying()?.complete()?.track?.id
        Log.d("AAA", songId.toString())
        api?.library?.add(LibraryType.TRACK, songId!!)?.complete()
    }
}