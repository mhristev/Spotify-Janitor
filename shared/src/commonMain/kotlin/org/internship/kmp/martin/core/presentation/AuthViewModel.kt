package org.internship.kmp.martin.core.presentation

import com.rickclephas.kmp.observableviewmodel.ViewModel
import org.internship.kmp.martin.core.data.database.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {
    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}