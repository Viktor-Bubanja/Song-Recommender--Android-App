package com.example.songrecommender

import android.os.AsyncTask
import com.adamratzman.spotify.SpotifyClientApi

class ResumeTrack(var api: SpotifyClientApi?): AsyncTask<Void, Void, Unit> () {
    override fun doInBackground(vararg params: Void) {
        if (!SpotifyService.isPlaying()) {
            api?.player?.resume()?.complete()
        }
    }
}