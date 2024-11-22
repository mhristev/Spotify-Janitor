package org.internship.kmp.martin.data.repository

import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserEntity
import org.internship.kmp.martin.data.domain.SpotifyUser
import  org.internship.kmp.martin.core.domain.Result

interface SpotifyUserRepository {
    suspend fun login(accessToken: String): Result<SpotifyUser,DataError.Remote>
    fun getCurrentSpotifyUser(): Flow<List<SpotifyUserEntity>>
    fun saveCurrentSpotifyUser(spotifyUser: SpotifyUser)
    fun logoutSpotifyUser()
    suspend fun addSpotifyUser(spotifyUser: SpotifyUser)
}