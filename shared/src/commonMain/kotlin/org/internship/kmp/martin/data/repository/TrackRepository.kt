package org.internship.kmp.martin.data.repository

import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.core.domain.Result

interface TrackRepository {
    suspend fun getFavoriteTracks(): Result<List<Track>, DataError>
    fun addFavoriteTrack(track: Track)
    fun removeFavoriteTrack(track: Track)
    suspend fun searchTracks(query: String) : Result<List<Track>, DataError>
    suspend fun synchronizeTracks(): Result<Flow<List<Track>>, DataError>
}