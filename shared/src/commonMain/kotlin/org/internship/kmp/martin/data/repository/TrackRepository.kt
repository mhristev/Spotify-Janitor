package org.internship.kmp.martin.data.repository

import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.core.domain.Result

interface TrackRepository {
    suspend fun getFavoriteTracks(): Result<List<Track>, DataError>
    suspend fun addFavoriteTrack(track: Track): Result<Unit, DataError>
    suspend fun removeFavoriteTrack(track: Track): Result<Unit, DataError>
    suspend fun searchTracks(query: String) : Result<List<Track>, DataError>
    suspend fun synchronizeTracks(): Result<Flow<List<Track>>, DataError>
}