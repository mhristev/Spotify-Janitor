package org.internship.kmp.martin.core.presentation

import com.rickclephas.kmp.observableviewmodel.ViewModel
import org.internship.kmp.martin.core.data.database.AuthRepository
import org.internship.kmp.martin.core.domain.DataError

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {

    suspend fun login(accessToken: String, expiresIn: Int): DataError? {
        return authRepository.login(accessToken, expiresIn)
    }
}