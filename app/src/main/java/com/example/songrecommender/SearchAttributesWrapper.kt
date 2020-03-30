package com.example.songrecommender

import com.adamratzman.spotify.endpoints.public.TrackAttribute
import com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute


class SearchAttributesWrapper(genre: String, emotion: Int, danceability: Int, acousticness: Int, var maxInput: Int) {
    var genre: String = genre.toLowerCase()
    val emotionAttribute: TrackAttribute<Float>? = createTrackAttribute(TuneableTrackAttribute.Valence, emotion)
    val danceabilityAttribute: TrackAttribute<Float>? = createTrackAttribute(TuneableTrackAttribute.Danceability, danceability)
    val acousticnessAttribute: TrackAttribute<Float>? = createTrackAttribute(TuneableTrackAttribute.Acousticness, acousticness)

    private fun createTrackAttribute(attribute: TuneableTrackAttribute<Float>, input: Int): TrackAttribute<Float>? {
        return when (input) {
            maxInput -> return TrackAttribute.create(attribute, HIGH)
            0 -> return TrackAttribute.create(attribute, LOW)
            else -> null
        }
    }
}

private const val HIGH: Float = 1.0f
private const val LOW: Float = 0.0f