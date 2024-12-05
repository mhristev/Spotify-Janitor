package org.internship.kmp.martin.track.presentation.browse_tracks

import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksAction

sealed interface BrowseTracksAction {
    data class onTrackAddToFavorites(val track: Track): BrowseTracksAction
    data class onSearch(val query: String): BrowseTracksAction
}