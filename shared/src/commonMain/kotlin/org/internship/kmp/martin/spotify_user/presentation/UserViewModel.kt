package org.internship.kmp.martin.spotify_user.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.spotify_user.data.database.SpotifyUserEntity
import org.internship.kmp.martin.spotify_user.data.repository.SpotifyUserRepository
import org.internship.kmp.martin.spotify_user.domain.SpotifyUser

class UserViewModel(private val userRepository: SpotifyUserRepository): ViewModel() {

    fun getCurrentSpotifyUser(): Flow<List<SpotifyUserEntity>> {
        return userRepository.getCurrentSpotifyUser()
    }
    fun logout() {
        userRepository.logoutSpotifyUser()
    }

    suspend fun addUser(spotifyUser: SpotifyUser) {
        userRepository.addSpotifyUser(spotifyUser)
    }
}