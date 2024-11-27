package org.internship.kmp.martin.track.presentation.fav_tracks_list

import org.internship.kmp.martin.track.domain.Track

data class FavoriteTracksState (
    val tracks: List<Track> = emptyList(),
    val lastRemovedTrack: Track? = null
)
