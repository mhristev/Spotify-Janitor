package org.internship.kmp.martin.track.presentation.fav_tracks_list

import org.internship.kmp.martin.track.domain.Track

sealed interface FavoriteTracksAction {
    data class onTrackDelete(val track: Track): FavoriteTracksAction
    data object onUndoDeleteTrack: FavoriteTracksAction
    data object SyncronizeTracks : FavoriteTracksAction
    data object GetNextFavoriteTracks : FavoriteTracksAction
}