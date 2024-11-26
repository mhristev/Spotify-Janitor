package org.internship.kmp.martin.presentation.fav_tracks_list

import org.internship.kmp.martin.data.domain.Track

data class FavoriteTracksState (
    val tracks: List<Track> = emptyList(),
    val lastRemovedTrack: Track? = null
)
