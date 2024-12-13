package org.internship.kmp.martin.track.presentation.fav_tracks_list

import org.internship.kmp.martin.track.domain.Track

sealed interface FavoriteTracksAction {
    data object OnSyncTracks : FavoriteTracksAction
    data object OnGetNextFavoriteTracks : FavoriteTracksAction
    data class OnRemoveTrackByIdGlobally(val trackId: String): FavoriteTracksAction
    data class OnRemoveTrackLocally(val track: Track): FavoriteTracksAction
    data object OnRestoreLastRemovedTrackLocally: FavoriteTracksAction
    data object OnHideUndoOption: FavoriteTracksAction
    data object OnShowDeletionDialog: FavoriteTracksAction
    data object OnHideDeletionDialog: FavoriteTracksAction
    data object OnErrorMessageShown: FavoriteTracksAction
}