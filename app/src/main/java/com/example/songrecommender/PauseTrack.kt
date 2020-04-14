package com.example.songrecommender

import android.os.AsyncTask
import com.adamratzman.spotify.SpotifyClientApi

class PauseTrack(var api: SpotifyClientApi?): AsyncTask<Void, Void, Unit> () {
    override fun doInBackground(vararg params: Void) {
        api?.player?.pause()?.complete()
    }
}