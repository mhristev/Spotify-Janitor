package org.internship.kmp.martin.core.presentation

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.internship.kmp.martin.core.data.database.AuthRepository
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksAction

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {

    @NativeCoroutinesState
    val isUserLoggedIn: StateFlow<Boolean> = authRepository.isUserLoggedIn()


    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.onLogin -> login(action.accessToken, action.expiresIn)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn().value
    }

    fun login(accessToken: String, expiresIn: Int) {
        viewModelScope.launch {
            val result = authRepository.login(accessToken, expiresIn)
        }
    }

}
