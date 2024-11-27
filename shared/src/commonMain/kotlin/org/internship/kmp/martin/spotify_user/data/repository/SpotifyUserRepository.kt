package org.internship.kmp.martin.spotify_user.data.repository

import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.core.domain.DataError
import  org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.spotify_user.data.database.SpotifyUserEntity
import org.internship.kmp.martin.spotify_user.domain.SpotifyUser

interface SpotifyUserRepository {
    suspend fun login(accessToken: String): Result<SpotifyUser,DataError.Remote>
    fun getCurrentSpotifyUser(): Flow<List<SpotifyUserEntity>>
    fun saveCurrentSpotifyUser(spotifyUser: SpotifyUser)
    fun logoutSpotifyUser()
    suspend fun addSpotifyUser(spotifyUser: SpotifyUser)
}