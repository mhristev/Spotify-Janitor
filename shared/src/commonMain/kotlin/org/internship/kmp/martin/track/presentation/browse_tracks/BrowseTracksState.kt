package org.internship.kmp.martin.track.presentation.browse_tracks

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.internship.kmp.martin.track.domain.Track

data class BrowseTracksState (
    val tracks: List<Track> = emptyList(),

    val isLoadingTracks: Boolean = false,
    val isSavingToFavoritesSuccess: Boolean = false,

    val errorString: String? = null
)