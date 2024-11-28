package org.internship.kmp.martin.core.data.network

import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.spotify_user.data.dto.SpotifyUserDto
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.track.data.dto.FavoriteTracksDto
import org.internship.kmp.martin.track.data.dto.SearchResponseDto

interface SpotifyApi {
    fun getCurrentUserId(): String?
    fun setToken(accessToken: String)
    suspend fun login(accessToken: String): Result<SpotifyUserDto, DataError.Remote>
    suspend fun getFavoriteTracks(limit: Int, offset: Int): Result<FavoriteTracksDto, DataError.Remote>
    suspend fun addFavoriteTrack(track: Track): Result<Unit, DataError.Remote>
    suspend fun removeFavoriteTrack(track: Track): Result<Unit, DataError.Remote>
    suspend fun searchTracksInSpotify(query: String): Result<SearchResponseDto, DataError.Remote>
}