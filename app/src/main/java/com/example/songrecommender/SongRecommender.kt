package com.example.songrecommender

import android.os.AsyncTask
import android.util.Log
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.endpoints.public.TrackAttribute
import com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.utils.Market
import java.lang.ref.WeakReference

class SongRecommender(activity: MainActivity, val attributes: SearchAttributesWrapper): AsyncTask<SpotifyClientApi, Void, List<Track>?> () {

    private var context = WeakReference(activity)

    override fun doInBackground(vararg apis: SpotifyClientApi?): List<Track>? {
        // Only one API will ever be passed into the function so simply take the first item from
        // the list.
        assert(apis.size == 1)
        val api = apis[0]

        val recommendedSongs = getRecommendedTracks(api)
        if (!recommendedSongs.isNullOrEmpty()) {
            playTrack(api, recommendedSongs)
        }
        return recommendedSongs
    }

    override fun onPostExecute(recommendedSongs: List<Track>?) {
        super.onPostExecute(recommendedSongs)
        if (recommendedSongs.isNullOrEmpty()) {
            context.get()?.showFailedToast()
        } else {
            context.get()?.showPlayer()
        }
    }

    private fun getRecommendedTracks(api: SpotifyClientApi?): List<Track>? {
        return api?.browse?.getTrackRecommendations(
            seedGenres = listOf(attributes.genre),
            targetAttributes = constructTrackAttributes(attributes),
            market = Market.NZ)?.complete()?.tracks
    }

    private fun constructTrackAttributes(attributes: SearchAttributesWrapper): List<TrackAttribute<Float>> {
        val trackAttributes = mutableListOf<TrackAttribute<Float>>()

        attributes.emotionAttribute?.let {trackAttributes.add(attributes.emotionAttribute)}
        attributes.danceabilityAttribute?.let {trackAttributes.add(attributes.danceabilityAttribute)}
        attributes.acousticnessAttribute?.let {trackAttributes.add(attributes.acousticnessAttribute)}

        return trackAttributes
    }

    private fun playTrack(api: SpotifyClientApi?, recommendedSongs: List<Track>) {
        recommendedSongs.shuffled().map { track -> track.uri.uri }
        val shuffledSongUris = recommendedSongs.shuffled().map { track -> track.uri.uri }
        Log.d("AAA", shuffledSongUris[0])
        val devices = api?.player?.getDevices()?.complete()
        Log.d("AAA", devices.toString())
        SpotifyService.play(shuffledSongUris, devices?.get(0)?.id.toString())
    }
}