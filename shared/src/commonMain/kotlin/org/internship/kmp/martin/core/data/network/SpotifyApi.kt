package org.internship.kmp.martin.core.data.network

import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.spotify_user.data.dto.SpotifyUserDto
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.track.data.dto.FavoriteTracksDto
import org.internship.kmp.martin.track.data.dto.SearchResponseDto

interface SpotifyApi {
    suspend fun getCurrentUser(): Result<SpotifyUserDto, DataError>
    suspend fun getFavoriteTracks(limit: Int = AppConstants.SpotifyApi.MAX_TRACKS_PER_CALL, offset: Int): Result<FavoriteTracksDto, DataError>
    suspend fun addFavoriteTrack(track: Track): Result<Unit, DataError>
    suspend fun removeFavoriteTrack(track: Track): Result<Unit, DataError>
    suspend fun searchTracksInSpotify(query: String): Result<SearchResponseDto, DataError>

}