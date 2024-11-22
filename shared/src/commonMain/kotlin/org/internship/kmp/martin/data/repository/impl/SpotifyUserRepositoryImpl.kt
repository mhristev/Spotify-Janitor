package org.internship.kmp.martin.data.repository.impl

import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.core.domain.map
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserDao
import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserEntity
import org.internship.kmp.martin.data.domain.SpotifyUser
import org.internship.kmp.martin.data.mappers.toDomain
import org.internship.kmp.martin.data.mappers.toEntity
import org.internship.kmp.martin.data.network.SpotifyApi
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.data.repository.SpotifyUserRepository

class SpotifyUserRepositoryImpl(private val userDao: SpotifyUserDao, private val apiClient: SpotifyApi): SpotifyUserRepository {
    override suspend fun login(accessToken: String): Result<SpotifyUser, DataError.Remote>{
        val result = apiClient.login(accessToken)
        result.onSuccess { currentUser ->
            userDao.upsert(currentUser.toDomain().toEntity())
        }
        return result.map { it.toDomain() }
        }

    override fun getCurrentSpotifyUser(): Flow<List<SpotifyUserEntity>> {
        return userDao.getAllUsers()
    }

    override fun saveCurrentSpotifyUser(spotifyUser: SpotifyUser) {
        TODO("Not yet implemented")
    }

    override fun logoutSpotifyUser() {
        TODO("Not yet implemented")
    }

    override suspend fun addSpotifyUser(spotifyUser: SpotifyUser) {
        TODO("Not yet implemented")
    }
}