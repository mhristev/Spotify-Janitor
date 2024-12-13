package org.internship.kmp.martin.track.presentation.fav_tracks_list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.internship.kmp.martin.track.domain.Track

data class FavoriteTracksState(
    val cashedTracksFlow: Flow<List<Track>> = flowOf(emptyList()),
    val trackToDelete: Track? = null,
    val errorString: String? = null,

    val isLoadingSync: Boolean = false,
    val isLoadingGetTracks: Boolean = false,
    val isShowingDeleteConfirmation: Boolean = false,
    val isShowingUndoButton: Boolean = false
)

