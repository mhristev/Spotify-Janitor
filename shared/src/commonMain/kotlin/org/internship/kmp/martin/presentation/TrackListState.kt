package org.internship.kmp.martin.presentation

import org.internship.kmp.martin.data.domain.Track

data class TrackListState (
    val tracks: List<Track> = emptyList()
)
