package org.internship.kmp.martin.spotify_user.presentation

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import org.internship.kmp.martin.core.data.database.AuthRepository
import org.internship.kmp.martin.spotify_user.data.repository.SpotifyUserRepository

class UserProfileViewModel(private val userRepository: SpotifyUserRepository, private val authRepository: AuthRepository): ViewModel() {
    private val _state = MutableStateFlow(UserProfileState())

    @NativeCoroutinesState
    val navigateToLogin = MutableStateFlow(false)

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


    fun onAction(action: UserProfileAction) {
        when (action) {
            is UserProfileAction.onGetCurrentUser -> getCurrentSpotifyUser()
            is UserProfileAction.onLogout -> logout()
        }
    }


    private fun getCurrentSpotifyUser() {
        viewModelScope.launch {
            val user = userRepository.getCurrentSpotifyUser()
            if (user != null) {
                _state.update {
                    it.copy(user = user)
                }
            }
        }
    }
    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            navigateToLogin.value = true
        }
    }

}