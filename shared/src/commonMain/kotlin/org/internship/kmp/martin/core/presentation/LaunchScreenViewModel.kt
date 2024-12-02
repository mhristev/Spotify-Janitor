package org.internship.kmp.martin.core.presentation

import com.rickclephas.kmp.observableviewmodel.ViewModel
import org.internship.kmp.martin.core.domain.onError
import kotlinx.coroutines.flow.MutableStateFlow
import org.internship.kmp.martin.spotify_user.data.repository.SpotifyUserRepository
//
//class LaunchScreenViewModel(private val userRepository: SpotifyUserRepository, private val authManager): ViewModel() {
//
//    val errorMessage =  MutableStateFlow<String?>(null)
//
//    suspend fun login(accessToken: String) {
//        userRepository.login(accessToken)
//            .onError { errorMessage.value = it.toString() }
//    }
//
//
//}