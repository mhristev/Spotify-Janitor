package org.internship.kmp.martin.core.data.database


import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
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
import org.internship.kmp.martin.track.data.database.FavoriteTrackDao

class AuthRepositoryImpl(private val authManager: AuthManager, private val apiClient: SpotifyApi, private val userDao: SpotifyUserDao, private val favoriteTrackDao: FavoriteTrackDao) : AuthRepository {
    val _isUserLoggedIn = MutableStateFlow(isUserAuthenticated())

    override fun isUserLoggedIn(): StateFlow<Boolean> = _isUserLoggedIn

    override fun isUserAuthenticated(): Boolean {
        authManager.getValidAccessToken()
            .onError {
                return false
            }
        return true
    }

    override suspend fun login(accessToken: String, expiresIn: Int): DataError? {
        authManager.setAccessToken(accessToken)
        authManager.setExpiresIn(expiresIn)
        val request = apiClient.getCurrentUser()
        request.onSuccess { currentUser ->
            authManager.setUserId(currentUser.id)
            userDao.upsert(currentUser.toDomain().toEntity())
            _isUserLoggedIn.value = true
        }
        request.onError { error ->
            return error
        }
        return null
    }

    override suspend fun logout() {
        authManager.logoutClear()
        favoriteTrackDao.removeAllFavTracks()
        _isUserLoggedIn.value = false
    }


}
