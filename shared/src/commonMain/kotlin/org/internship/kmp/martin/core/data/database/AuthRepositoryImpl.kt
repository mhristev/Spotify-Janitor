package org.internship.kmp.martin.core.data.database

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import org.internship.kmp.martin.core.data.auth.AuthManager
import org.internship.kmp.martin.core.data.network.SpotifyApi
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.core.domain.map
import org.internship.kmp.martin.core.domain.onError
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.spotify_user.data.database.SpotifyUserDao
import org.internship.kmp.martin.spotify_user.data.mappers.toDomain
import org.internship.kmp.martin.spotify_user.data.mappers.toEntity
import org.internship.kmp.martin.spotify_user.domain.SpotifyUser

class AuthRepositoryImpl(private val authManager: AuthManager, private val apiClient: SpotifyApi, private val userDao: SpotifyUserDao) : AuthRepository {

    private val _isUserLoggedIn = MutableStateFlow(false)
    override fun isUserLoggedIn(): StateFlow<Boolean> = _isUserLoggedIn


//    override fun isUserLoggedIn(): StateFlow<Boolean> {
//        val isLoggedIn = authManager.getAccessToken() != null
//        return MutableStateFlow(isLoggedIn)
//    }

    override suspend fun login(accessToken: String, expiresIn: Int): DataError? {
        authManager.setAccessToken(accessToken)
        val request = apiClient.getCurrentUser()
        request.onSuccess { currentUser ->
            authManager.login(accessToken, currentUser.id, expiresIn)
            userDao.upsert(currentUser.toDomain().toEntity())
            _isUserLoggedIn.value = true
        }
        request.onError { error ->
            return error
        }
        return null
    }

    override fun logout() {
        authManager.logoutClear()
        _isUserLoggedIn.value = false
    }

}