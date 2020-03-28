package com.example.songrecommender

import android.os.AsyncTask
import android.util.Log
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.endpoints.public.TrackAttribute
import com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.utils.Market
import java.lang.ref.WeakReference

class SongRecommender(activity: MainActivity): AsyncTask<SpotifyClientApi, Void, Void> () {

    private var context = WeakReference(activity)

    override fun doInBackground(vararg apis: SpotifyClientApi?): Void? {
        // Only one API will ever be passed into the function so simply take the first item from
        // the list.
        assert(apis.size == 1)
        val api = apis[0]

        val recommendedSongs = getRecommendedTracks(api)
        if (!recommendedSongs.isNullOrEmpty()) {
            playTrack(api, recommendedSongs)
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        context.get()?.showPlayer()
    }

    private fun getRecommendedTracks(api: SpotifyClientApi?): List<Track>? {
        return api?.browse?.getTrackRecommendations(
            seedGenres = listOf("pop"),
            targetAttributes = listOf(
                TrackAttribute.create(TuneableTrackAttribute.Danceability, 0.5f),
                TrackAttribute.create(TuneableTrackAttribute.Valence, 0.2f)),
            market = Market.NZ)?.complete()?.tracks
    }

    private fun playTrack(api: SpotifyClientApi?, recommendedSongs: List<Track>) {
        recommendedSongs.shuffled().map { track -> track.uri.uri }
        val shuffledSongUris = recommendedSongs.shuffled().map { track -> track.uri.uri }
        Log.d("AAA", shuffledSongUris[0].toString())
        val devices = api?.player?.getDevices()?.complete()
        Log.d("AAA", devices.toString())
        api?.player?.startPlayback(
            tracksToPlay = shuffledSongUris,
            deviceId = devices?.get(0)?.id.toString())?.complete()
    }
}