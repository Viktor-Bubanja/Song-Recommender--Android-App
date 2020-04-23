package com.example.songrecommender

import android.os.AsyncTask
import android.util.Log
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.endpoints.public.TrackAttribute
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.utils.Market
import java.lang.ref.WeakReference

class SongRecommender(activity: MainActivity,
                      val attributes: SearchAttributesWrapper,
                      var api: SpotifyClientApi?): AsyncTask<Void, Void, List<Track>?> () {

    private var context = WeakReference(activity)

    override fun doInBackground(vararg params: Void): List<Track>? {
        return try {
            val recommendedSongs = getRecommendedTracks(api)
            if (!recommendedSongs.isNullOrEmpty()) {
                playTrack(recommendedSongs)
            }
            recommendedSongs
        } catch (e: Exception) {
            Log.e("SongRecommender", e.message)
            emptyList()
        }
    }

    override fun onPostExecute(recommendedSongs: List<Track>?) {
        super.onPostExecute(recommendedSongs)
        if (recommendedSongs.isNullOrEmpty()) {
            context.get()?.onSongSearchFail()
        } else {
            context.get()?.onSongSearchSuccess()
        }
    }

    private fun getRecommendedTracks(api: SpotifyClientApi?): List<Track>? {
        val tracksToPlay = api?.browse?.getTrackRecommendations(
            seedGenres = listOf(attributes.genre),
            targetAttributes = constructTrackAttributes(attributes),
            market = Market.NZ)?.complete()?.tracks
        SpotifyService.tracksToPlay = tracksToPlay
        return tracksToPlay
    }

    private fun constructTrackAttributes(attributes: SearchAttributesWrapper): List<TrackAttribute<Float>> {
        val trackAttributes = mutableListOf<TrackAttribute<Float>>()

        attributes.emotionAttribute?.let {trackAttributes.add(attributes.emotionAttribute)}
        attributes.danceabilityAttribute?.let {trackAttributes.add(attributes.danceabilityAttribute)}
        attributes.acousticnessAttribute?.let {trackAttributes.add(attributes.acousticnessAttribute)}

        return trackAttributes
    }

    private fun playTrack(recommendedSongs: List<Track>) {
        recommendedSongs.shuffled().map { track -> track.uri.uri }
        val shuffledSongUris = recommendedSongs.shuffled().map { track -> track.uri.uri }
        SpotifyService.play(shuffledSongUris)
    }
}