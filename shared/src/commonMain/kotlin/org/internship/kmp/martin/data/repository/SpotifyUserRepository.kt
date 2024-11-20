package org.internship.kmp.martin.data.repository

import org.internship.kmp.martin.data.domain.SpotifyUser

interface SpotifyUserRepository {
    fun getCurrentSpotifyUser(): SpotifyUser
    fun saveCurrentSpotifyUser(spotifyUser: SpotifyUser)
    fun logoutSpotifyUser()
}