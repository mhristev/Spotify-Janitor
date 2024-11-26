package org.internship.kmp.martin.presentation.fav_tracks_list

import org.internship.kmp.martin.data.domain.Track

sealed interface FavoriteTracksAction {
    data class onTrackDelete(val track: Track): FavoriteTracksAction
    data object onUndoDeleteTrack: FavoriteTracksAction
    data object SyncronizeTracks : FavoriteTracksAction
}