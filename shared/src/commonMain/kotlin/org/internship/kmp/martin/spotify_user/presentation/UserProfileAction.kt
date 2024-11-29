package org.internship.kmp.martin.spotify_user.presentation

sealed interface UserProfileAction {
    data object onGetCurrentUser: UserProfileAction
    data object onLogout: UserProfileAction
}