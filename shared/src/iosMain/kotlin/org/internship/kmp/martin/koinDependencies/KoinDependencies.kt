package org.internship.kmp.martin.koinDependencies

import org.internship.kmp.martin.core.presentation.AuthViewModel
import org.internship.kmp.martin.core.presentation.LoginViewModel
import org.internship.kmp.martin.di.MyTestInterface
import org.internship.kmp.martin.spotify_user.presentation.UserProfileViewModel
import org.internship.kmp.martin.track.data.repository.TrackRepository
import org.internship.kmp.martin.track.presentation.browse_tracks.BrowseTracksViewModel
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.component.get

object KoinDependencies : KoinComponent {
//        fun getTrackRepository(): TrackRepository = get()
//        fun myTestInterface(): MyTestInterface = get()
//        fun getFavoriteTracksViewModel(): FavoriteTracksViewModel = get()
        val favoriteTracksViewModel: FavoriteTracksViewModel by inject()
        val authViewModel: AuthViewModel by inject()
        val userProfileViewModel: UserProfileViewModel by inject()
        val browseTracksViewModel: BrowseTracksViewModel by inject()
}
