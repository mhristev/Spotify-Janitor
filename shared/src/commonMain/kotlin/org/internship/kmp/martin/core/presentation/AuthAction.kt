package org.internship.kmp.martin.core.presentation

import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksAction

sealed interface AuthAction {
    data class onLogin(val accessToken: String, val expiresIn: Int): AuthAction
}