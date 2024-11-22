package org.internship.kmp.martin.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserEntity
import org.internship.kmp.martin.data.domain.SpotifyUser
import org.internship.kmp.martin.data.repository.SpotifyUserRepository

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