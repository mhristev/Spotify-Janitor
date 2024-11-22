package org.internship.kmp.martin.presentation

import com.rickclephas.kmp.observableviewmodel.ViewModel
import org.internship.kmp.martin.core.domain.onError
import org.internship.kmp.martin.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import org.internship.kmp.martin.data.repository.SpotifyUserRepository

class LaunchScreenViewModel(private val userRepository: SpotifyUserRepository): ViewModel() {

    val errorMessage =  MutableStateFlow<String?>(null)

    suspend fun login(accessToken: String) {
        userRepository.login(accessToken)
            .onError { errorMessage.value = it.toString() }
    }

}