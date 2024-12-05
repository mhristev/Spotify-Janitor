package org.internship.kmp.martin.track.presentation.browse_tracks

import org.internship.kmp.martin.track.domain.Track

data class BrowseTracksState (
    val tracks: List<Track> = emptyList()
)