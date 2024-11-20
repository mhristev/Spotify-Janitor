package org.internship.kmp.martin.data.repository.impl

import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserDao
import org.internship.kmp.martin.data.domain.SpotifyUser
import org.internship.kmp.martin.data.network.SpotifyApi
import org.internship.kmp.martin.data.repository.SpotifyUserRepository
import org.internship.kmp.martin.data.repository.images.ImageManager

class SpotifyUserRepositoryImpl(private val userDao: SpotifyUserDao, apiClient: SpotifyApi, imageManager: ImageManager<SpotifyUser>): SpotifyUserRepository {
    override fun getCurrentSpotifyUser(): SpotifyUser {
        TODO("Not yet implemented")
    }

    override fun saveCurrentSpotifyUser(spotifyUser: SpotifyUser) {
        TODO("Not yet implemented")
    }

    override fun logoutSpotifyUser() {
        TODO("Not yet implemented")
    }

}