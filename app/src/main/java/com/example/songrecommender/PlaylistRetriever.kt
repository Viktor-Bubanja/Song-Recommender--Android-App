package com.example.songrecommender

import android.os.AsyncTask
import android.util.Log
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.models.SimplePlaylist
import java.lang.ref.WeakReference

class PlaylistRetriever(val activity: PlaylistActivity): AsyncTask<SpotifyClientApi, Void, List<SimplePlaylist>>() {

    private var context = WeakReference(activity)


    override fun doInBackground(vararg apis: SpotifyClientApi?): List<SimplePlaylist>? {
        // Only one API will ever be passed into the function so simply take the first item from
        // the list.
        assert(apis.size == 1)
        val api = apis[0]
//        Log.d("AAA", api?.playlists?.getClientPlaylists(limit=10)?.getAllItems()?.complete().toString())
        val a = api?.playlists?.getClientPlaylists(limit=1)?.getAllItems()?.complete()
        Log.d("AAA", a.toString())
        return api?.playlists?.getClientPlaylists(limit=1)?.getAllItems()?.complete()
    }

    override fun onPostExecute(playlists: List<SimplePlaylist>) {
        super.onPostExecute(playlists)
        Log.d("AAA", "onposteecute")
        Log.d("AAA", playlists.toString())
        context.get()?.playlists = playlists
    }

}