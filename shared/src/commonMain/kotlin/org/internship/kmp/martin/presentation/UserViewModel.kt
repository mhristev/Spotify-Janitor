package org.internship.kmp.martin.presentation

import androidx.lifecycle.ViewModel
import org.internship.kmp.martin.data.domain.SpotifyUser
import org.internship.kmp.martin.data.repository.SpotifyUserRepository

class UserViewModel(private val userRepository: SpotifyUserRepository): ViewModel() {

    fun getCurrentSpotifyUser(): SpotifyUser {
        return userRepository.getCurrentSpotifyUser()
    }
    fun logout() {
        userRepository.logoutSpotifyUser()
    }
}