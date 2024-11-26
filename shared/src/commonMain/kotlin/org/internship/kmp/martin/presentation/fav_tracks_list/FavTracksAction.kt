package org.internship.kmp.martin.presentation.fav_tracks_list

import org.internship.kmp.martin.data.domain.Track

sealed interface FavTracksAction {
    data class onTrackDelete(val track: Track): FavTracksAction
    data object onUndoDeleteTrack: FavTracksAction
}