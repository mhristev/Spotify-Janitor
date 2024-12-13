package org.internship.kmp.martin.track.presentation.browse_tracks

import org.internship.kmp.martin.track.domain.Track

sealed interface BrowseTracksAction {
    data class onTrackAddToFavorites(val track: Track): BrowseTracksAction
    data class onSearch(val query: String): BrowseTracksAction
    data object OnErrorMessageShown: BrowseTracksAction
    data object OnSaveSuccessShown: BrowseTracksAction
}