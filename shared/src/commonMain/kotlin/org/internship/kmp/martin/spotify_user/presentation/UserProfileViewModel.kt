package org.internship.kmp.martin.spotify_user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.spotify_user.data.repository.SpotifyUserRepository
import org.internship.kmp.martin.spotify_user.domain.SpotifyUser
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksState

class UserProfileViewModel(private val userRepository: SpotifyUserRepository): ViewModel() {
    private val _state = MutableStateFlow(UserProfileState())

    @NativeCoroutinesState
    val state = _state
        .onStart {
            getCurrentSpotifyUser()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )


    fun getCurrentSpotifyUser() {
        viewModelScope.launch {
            val user = userRepository.getCurrentSpotifyUser()
            if (user != null) {
                _state.update {
                    it.copy(user = user)
                }
            }
        }
    }
    fun logout() {
        userRepository.logoutSpotifyUser()
    }

}