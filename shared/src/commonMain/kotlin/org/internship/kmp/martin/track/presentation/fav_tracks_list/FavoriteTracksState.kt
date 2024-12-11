package org.internship.kmp.martin.track.presentation.fav_tracks_list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.internship.kmp.martin.track.domain.Track

data class FavoriteTracksState(
    val tracks: Flow<List<Track>> = flowOf(emptyList()),
    val lastRemovedTrack: Track? = null,
    val errorString: String? = null,
)

