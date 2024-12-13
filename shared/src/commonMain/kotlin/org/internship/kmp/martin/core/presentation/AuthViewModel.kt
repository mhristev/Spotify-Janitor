package org.internship.kmp.martin.core.presentation

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesRefinedState
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import org.internship.kmp.martin.core.data.database.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {
    @NativeCoroutinesRefinedState
    val isUserLoggedIn: StateFlow<Boolean> = authRepository.isUserLoggedIn()

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.onLogin -> login(action.accessToken, action.expiresIn)
        }
    }

    private fun login(accessToken: String, expiresIn: Int) {
        viewModelScope.launch {
            val result = authRepository.login(accessToken, expiresIn)
        }
    }

}
