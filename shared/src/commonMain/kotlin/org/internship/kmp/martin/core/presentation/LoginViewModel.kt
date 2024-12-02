package org.internship.kmp.martin.core.presentation

import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.launch
import org.internship.kmp.martin.core.data.database.AuthRepository
import org.internship.kmp.martin.core.domain.DataError

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {

    fun login(accessToken: String, expiresIn: Int) {
        viewModelScope.launch {
            val result = authRepository.login(accessToken, expiresIn)
        }
    }
}