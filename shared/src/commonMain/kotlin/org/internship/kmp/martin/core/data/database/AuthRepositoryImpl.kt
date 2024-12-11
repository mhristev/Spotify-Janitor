package org.internship.kmp.martin.core.data.database


import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.internship.kmp.martin.core.data.auth.AuthManager
import org.internship.kmp.martin.core.data.network.SpotifyApi
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.core.domain.onError
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.spotify_user.data.database.SpotifyUserDao
import org.internship.kmp.martin.spotify_user.data.mappers.toDomain
import org.internship.kmp.martin.spotify_user.data.mappers.toEntity

class AuthRepositoryImpl(private val authManager: AuthManager, private val apiClient: SpotifyApi, private val userDao: SpotifyUserDao) : AuthRepository {
//    @NativeCoroutinesState
    val _isUserLoggedIn = MutableStateFlow(isUserAuthenticated())


    override fun isUserLoggedIn(): StateFlow<Boolean> = _isUserLoggedIn
    @NativeCoroutines
    val bla =  isUserLoggedIn()

    override fun isUserAuthenticated(): Boolean {
        val isLoggedIn = authManager.getAccessToken() != null && !authManager.hasTokenExpired().value
        return isLoggedIn
    }

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
