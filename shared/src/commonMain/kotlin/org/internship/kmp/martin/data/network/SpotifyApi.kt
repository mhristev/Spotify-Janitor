package org.internship.kmp.martin.data.network

import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.data.dto.SpotifyUserDto
import org.internship.kmp.martin.data.dto.TrackDto
import org.internship.kmp.martin.core.domain.Result

interface SpotifyApi {
    fun setToken(accessToken: String)
    suspend fun login(accessToken: String): Result<SpotifyUserDto, DataError.Remote>
    fun getFavoriteTracks(): List<TrackDto>
    fun addFavoriteTrack(track: Track)
    fun removeFavoriteTrack(track: Track)
}