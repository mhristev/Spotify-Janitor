package org.internship.kmp.martin.track.presentation.fav_tracks_list

import org.internship.kmp.martin.track.domain.Track

data class FavoriteTracksState(
    val cachedTracks: List<Track> = emptyList(),
    val errorString: String? = null,

    val isLoadingSync: Boolean = false,
    val isLoadingNextTracks: Boolean = false,
    val isShowingUndoButton: Boolean = false
)

