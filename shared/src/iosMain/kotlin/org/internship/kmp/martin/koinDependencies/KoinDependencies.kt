package org.internship.kmp.martin.koinDependencies

import org.internship.kmp.martin.core.data.database.AuthRepository
import org.internship.kmp.martin.core.presentation.AuthViewModel
import org.internship.kmp.martin.core.presentation.LoginViewModel
import org.internship.kmp.martin.spotify_user.presentation.UserProfileViewModel
import org.internship.kmp.martin.track.data.repository.TrackRepository
import org.internship.kmp.martin.track.presentation.browse_tracks.BrowseTracksViewModel
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.component.get

object KoinDependencies : KoinComponent {
        fun getFavoriteTracksViewModel() = getKoin().get<FavoriteTracksViewModel>()
        fun getAuthViewModel() = getKoin().get<AuthViewModel>()
        fun getUserProfileViewModel() = getKoin().get<UserProfileViewModel>()
        fun getBrowseTracksViewModel() = getKoin().get<BrowseTracksViewModel>()
        fun getAuthRepository() = getKoin().get<AuthRepository>()
        fun getTrackRepository() = getKoin().get<TrackRepository>()
}
