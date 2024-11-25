package org.internship.kmp.martin.data.network

import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.data.dto.SpotifyUserDto
import org.internship.kmp.martin.data.dto.TrackDto
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.data.dto.FavoriteTracksDto
import org.internship.kmp.martin.data.dto.SearchResponseDto

interface SpotifyApi {
    fun setToken(accessToken: String)
    suspend fun login(accessToken: String): Result<SpotifyUserDto, DataError.Remote>
    suspend fun getFavoriteTracks(limit: Int, offset: Int): Result<FavoriteTracksDto, DataError.Remote>
    fun addFavoriteTrack(track: Track)
    fun removeFavoriteTrack(track: Track)
    suspend fun searchTracksInSpotify(query: String): Result<SearchResponseDto, DataError.Remote>
}