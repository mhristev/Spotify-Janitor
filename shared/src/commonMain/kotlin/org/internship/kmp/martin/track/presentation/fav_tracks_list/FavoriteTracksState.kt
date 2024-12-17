package org.internship.kmp.martin.track.presentation.fav_tracks_list

import org.internship.kmp.martin.track.domain.Track

data class FavoriteTracksState(
    val cashedTracks: List<Track> = emptyList(),
    val trackToDelete: Track? = null,
    val errorString: String? = null,

    val isLoadingSync: Boolean = false,
    val isLoadingGetTracks: Boolean = false,
    val isShowingDeleteConfirmation: Boolean = false,
    val isShowingUndoButton: Boolean = false
)

