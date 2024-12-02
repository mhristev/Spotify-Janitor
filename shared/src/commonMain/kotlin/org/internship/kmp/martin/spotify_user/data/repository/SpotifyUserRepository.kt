package org.internship.kmp.martin.spotify_user.data.repository

import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.core.domain.DataError
import  org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.spotify_user.data.database.SpotifyUserEntity
import org.internship.kmp.martin.spotify_user.domain.SpotifyUser

interface SpotifyUserRepository {
    suspend fun getCurrentSpotifyUser(): SpotifyUser?
}