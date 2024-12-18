package org.internship.kmp.martin.track.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.track.data.database.FavoriteTrackDao
import org.internship.kmp.martin.core.data.network.SpotifyApi
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.track.data.repository.TrackRepository
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.core.domain.onError
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.track.data.dto.mapAddedAt
import org.internship.kmp.martin.track.data.mappers.toDomain
import org.internship.kmp.martin.track.data.mappers.toEntity

class TrackRepositoryImpl(private val trackDao: FavoriteTrackDao, private val spotifyApi: SpotifyApi):
    TrackRepository {

    override suspend fun checkAndFetchFavoriteTracks(currentTrackCount: Int, increaseWith: Int): Result<Unit, DataError> {
        val localTracks = trackDao.getFavoriteTracksHandle()

        if (localTracks.size > currentTrackCount && (localTracks.size - currentTrackCount) >= increaseWith) {
            return Result.Success(Unit)
        } else {
            fetchAndStoreAdditionalTracks(currentTrackCount)
                .onError {
                    return Result.Error(it)
                }
                .onSuccess {
                    return Result.Success(Unit)
                }
        }
        return Result.Error(DataError.Local.UNKNOWN)
    }


    override suspend fun getFavoriteTracksFlow(limit: Int): Flow<List<Track>> {
        return trackDao.getFavoriteTracksPaged(limit)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun restoreTrackLocally(track: Track) {
        trackDao.upsert(track.toEntity())
    }

    private suspend fun fetchAndStoreAdditionalTracks(currentLocalCount: Int): Result<Unit, DataError> {
        val result = spotifyApi.getFavoriteTracks(offset = currentLocalCount)
        result.onSuccess { favTracksDto ->
            favTracksDto.mapAddedAt()
            favTracksDto.items.forEach { trackDto ->
                trackDao.upsert(trackDto.track.toDomain().toEntity())
            }
            return Result.Success(Unit)
        }.onError {
            return Result.Error(it)
        }
        return Result.Error(DataError.Local.UNKNOWN)
    }


    override suspend fun addTrackToFavorites(track: Track): Result<Unit, DataError> {

        spotifyApi.addFavoriteTrack(track)
            .onError {
                return Result.Error(it)
            }

        trackDao.upsert(track.toEntity())
        return Result.Success(Unit)
    }

    override suspend fun removeFavoriteTrackLocally(track: Track) {
        trackDao.removeFavTrack(track.id)
    }

    override suspend fun removeFavoriteTrackGlobally(track: Track): Result<Unit, DataError> {
        trackDao.removeFavTrack(track.id)
        spotifyApi.removeFavoriteTrack(track)
            .onError {
                return Result.Error(it)
            }
        return Result.Success(Unit)
    }

    override suspend fun searchTracks(query: String): Result<List<Track>, DataError> {
        spotifyApi.searchTracksInSpotify(query)
            .onSuccess { searchResponseDto ->
                val tracks = searchResponseDto.tracks.items.map { it.toDomain() }
                return Result.Success(tracks)
            }
            .onError {
                return Result.Error(it)
            }
        return Result.Error(DataError.Local.UNKNOWN)
    }

    override suspend fun syncAllTracks(): Result<Unit, DataError> {
        val localTracks = trackDao.getFavoriteTracksHandle().map { it.toDomain() }
        getTracksFromApi(localTracks.size)
            .onSuccess { apiTracks ->
                synchronizeLocalTracksWithApi(localTracks, apiTracks)
            }
            .onError {
                return Result.Error(it)
            }
            return Result.Success(Unit)

    }

    private suspend fun getTracksFromApi(localTracksCount: Int): Result<List<Track>, DataError> {
        val maxTracksPerCall: Int = AppConstants.SpotifyApi.MAX_TRACKS_PER_CALL
        var counter = 0
        val result = mutableListOf<Track>()

        while (counter < localTracksCount) {
            spotifyApi.getFavoriteTracks(offset = counter)
                .onSuccess { favTracksDto ->
                    favTracksDto.mapAddedAt()
                    val apiTracks = favTracksDto.items.map { it.track.toDomain() }
                    result.addAll(apiTracks)
                }
                .onError {
                    return Result.Error(it)
                }
            counter += maxTracksPerCall
        }
        return Result.Success(result)

    }

    private suspend fun synchronizeLocalTracksWithApi(localTracks: List<Track>, apiTracks: List<Track>) {
        localTracks.filterNot { localTrack -> apiTracks.any { it.id == localTrack.id } }.forEach { track ->
            trackDao.removeFavTrack(track.id)
        }

        // Add tracks from the API that are not in the local database
        apiTracks.filterNot {
            apiTrack -> localTracks.any { it.id == apiTrack.id } }.forEach {
                track ->
            trackDao.upsert(track.toEntity())
        }

        // Update the addedAt factor for tracks that exist in both the local DB and API
        apiTracks.filter { track -> localTracks.any { it.id == track.id } }.forEach { track ->
            val localTrack = localTracks.first { it.id == track.id }
            if (track.addedAt != localTrack.addedAt) {
                trackDao.upsert(track.toEntity())
            }
        }
    }
}
