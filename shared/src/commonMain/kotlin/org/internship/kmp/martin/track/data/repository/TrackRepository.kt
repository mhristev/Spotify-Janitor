package org.internship.kmp.martin.track.data.repository

import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.core.domain.Result

interface TrackRepository {
    suspend fun isSongInFavorites(track: Track): Boolean
    suspend fun getFavoriteTracks(): Result<Flow<List<Track>>, DataError>
    suspend fun addFavoriteTrack(track: Track): Result<Unit, DataError>
    suspend fun removeFavoriteTrackLocally(track: Track)
    suspend fun removeFavoriteTrackGlobally(track: Track): Result<Unit, DataError>
    suspend fun searchTracks(query: String) : Result<List<Track>, DataError>
    suspend fun synchronizeTracks(): Result<Flow<List<Track>>, DataError>
    suspend fun getNextFavoriteTracks(currentTrackOffset: Int): Result<Flow<List<Track>>, DataError>
    suspend fun restoreTrackToDao(track: Track)
    suspend fun removeFavoriteTrackById(trackId: String)
    suspend fun fetchAndStoreAdditionalTracks(currentLocalCount: Int)
    suspend fun getLocalFavoriteTracksCount(): Int
    fun getFavoriteTracksPaged(limit: Int, offset: Int): Flow<List<Track>>
    suspend fun syncTracksReturnOffset(offset: Int)
}