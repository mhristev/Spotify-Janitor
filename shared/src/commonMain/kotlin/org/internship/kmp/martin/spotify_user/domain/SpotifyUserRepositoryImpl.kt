package org.internship.kmp.martin.spotify_user.domain

import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.core.data.auth.AuthManager
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.core.domain.map
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.spotify_user.data.mappers.toDomain
import org.internship.kmp.martin.spotify_user.data.mappers.toEntity
import org.internship.kmp.martin.core.data.network.SpotifyApi
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.spotify_user.data.database.SpotifyUserDao
import org.internship.kmp.martin.spotify_user.data.database.SpotifyUserEntity
import org.internship.kmp.martin.spotify_user.data.repository.SpotifyUserRepository

class SpotifyUserRepositoryImpl(private val userDao: SpotifyUserDao, private val authManager: AuthManager) :
    SpotifyUserRepository {

    override suspend fun getCurrentSpotifyUser(): SpotifyUser? {
        val currentUserId = authManager.getUserId() ?: return null
        return userDao.getUserById(currentUserId)?.toDomain()
    }

}