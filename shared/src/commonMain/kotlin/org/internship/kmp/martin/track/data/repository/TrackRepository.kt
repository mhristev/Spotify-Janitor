package org.internship.kmp.martin.track.data.repository

import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.core.domain.Result

interface TrackRepository {
    suspend fun getNextFavoriteTracks(currentTrackOffset: Int): Result<Flow<List<Track>>, DataError>
    suspend fun fetchInitialFavoriteTracks(): Result<Flow<List<Track>>, DataError>

    suspend fun searchTracks(query: String) : Result<List<Track>, DataError>

    suspend fun syncAllTracks(): Result<Unit, DataError>

    suspend fun removeFavoriteTrackGlobally(track: Track): Result<Unit, DataError>
    suspend fun addTrackToFavorites(track: Track): Result<Unit, DataError>

    suspend fun removeFavoriteTrackLocally(track: Track)
    suspend fun restoreTrackLocally(track: Track)


}