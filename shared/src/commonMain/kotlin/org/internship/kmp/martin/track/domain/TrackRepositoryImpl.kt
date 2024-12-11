package org.internship.kmp.martin.track.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.track.data.database.FavoriteTrackDao
import org.internship.kmp.martin.core.data.network.SpotifyApi
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.core.domain.NetworkConstants
import org.internship.kmp.martin.track.data.repository.TrackRepository
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.core.domain.onError
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.track.data.dto.mapAddedAt
import org.internship.kmp.martin.track.data.database.TrackEntity
import org.internship.kmp.martin.track.data.mappers.toDomain
import org.internship.kmp.martin.track.data.mappers.toEntity

class TrackRepositoryImpl(private val trackDao: FavoriteTrackDao, private val spotifyApi: SpotifyApi):
    TrackRepository {

    override suspend fun isSongInFavorites(track: Track): Boolean {
        return trackDao.getFavoriteTracks().firstOrNull()?.any { it.id == track.id } ?: false
    }

    override suspend fun getFavoriteTracks(): Result<Flow<List<Track>>, DataError> {
        val limitTracksPerCall = NetworkConstants.Limits.TRACKS_PER_CALL
        val minTracksDisplayed = AppConstants.Limits.MIN_TRACKS_TO_DISPLAY

        val trackCount = trackDao.getFavoriteTracksHandle().size
            val tracksFlow = trackDao.getFavoriteTracks()
                .map { it.map { trackEntity -> trackEntity.toDomain() } }

        return if (trackCount < minTracksDisplayed) {
            fetchAndStoreFavoriteTracks(limitTracksPerCall)
        } else {
            Result.Success(tracksFlow)
        }
    }


    private suspend fun fetchAndStoreFavoriteTracks(limitTracksPerCall: Int): Result<Flow<List<Track>>, DataError> {
        val result = spotifyApi.getFavoriteTracks(limitTracksPerCall, 0)
        result.onSuccess { favTracksDto ->
            favTracksDto.mapAddedAt()
            favTracksDto.items.forEach { trackDto ->
                trackDao.upsert(trackDto.track.toDomain().toEntity())
            }
            val tracksFlow = trackDao.getFavoriteTracks()
                .map { it.map { trackEntity -> trackEntity.toDomain() } }
            return Result.Success(tracksFlow)
        }.onError {
            return Result.Error(it)
        }
        return Result.Error(DataError.Local.UNKNOWN)
    }

    override suspend fun getNextFavoriteTracks(currentTrackOffset: Int) {
        val localTracks = trackDao.getFavoriteTracksHandle()

        if (localTracks.size > currentTrackOffset) {
            getLocalFavoriteTracks(localTracks, currentTrackOffset)
        } else {
            fetchAndStoreAdditionalTracks(currentTrackOffset)
        }
    }

    override suspend fun restoreTrackToDao(track: Track) {
        trackDao.upsert(track.toEntity())
    }


    override suspend fun removeFavoriteTrackById(trackId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAndStoreAdditionalTracks(currentLocalCount: Int) {
        val limitTracksPerCall = NetworkConstants.Limits.TRACKS_PER_CALL

        val result = spotifyApi.getFavoriteTracks(limitTracksPerCall, currentLocalCount)
        result.onSuccess { favTracksDto ->
            favTracksDto.mapAddedAt()
            favTracksDto.items.forEach { trackDto ->
                trackDao.upsert(trackDto.track.toDomain().toEntity())
            }
        }.onError {
        }
    }

    override suspend fun getLocalFavoriteTracksCount(): Int {
        return trackDao.getFavoriteTracksHandle().size
    }

    private fun getLocalFavoriteTracks(localTracks: List<TrackEntity>, currentTrackOffset: Int) {
//        val tracksPerLoadMore = AppConstants.Limits.TRACKS_PER_LOAD_MORE
//        val domainTracks = localTracks.drop(currentTrackOffset).take(tracksPerLoadMore).map { it.toDomain() }
//        return domainTracks)
    }

    private suspend fun getRemoteFavoriteTracks(currentTrackOffset: Int): Result<Flow<List<Track>>, DataError> {
        val limitTracksPerCall = NetworkConstants.Limits.TRACKS_PER_CALL
        val result = spotifyApi.getFavoriteTracks(limitTracksPerCall, currentTrackOffset)
        result.onSuccess { favTracksDto ->
            favTracksDto.mapAddedAt()
            favTracksDto.items.forEach { trackDto ->
                trackDao.upsert(trackDto.track.toDomain().toEntity())
            }
            getNextFavoriteTracks(currentTrackOffset)
        }.onError {
            return Result.Error(it)
        }
        return Result.Error(DataError.Local.UNKNOWN)
    }

    override suspend fun addFavoriteTrack(track: Track): Result<Unit, DataError> {
        trackDao.upsert(track.toEntity())
        spotifyApi.addFavoriteTrack(track)
            .onError {
                return Result.Error(it)
            }
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
       val result = spotifyApi.searchTracksInSpotify(query)
        result.onSuccess { searchResponseDto ->
            val tracks = searchResponseDto.tracks.items.map { it.toDomain() }
            return Result.Success(tracks)
        }.onError {
            return Result.Error(it)
        }
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun synchronizeTracks(): Result<Flow<List<Track>>, DataError> {
        val localTracks = trackDao.getFavoriteTracks().firstOrNull() ?: emptyList()
        val allApiTracks = fetchApiTracks(localTracks.size)

        if (allApiTracks is Result.Error) {
            return allApiTracks
        }

        val apiTracks = (allApiTracks as Result.Success).data
        synchronizeLocalTracksWithApi(localTracks, apiTracks)

        val domainTracks = trackDao.getFavoriteTracks().map { it.map { trackEntity -> trackEntity.toDomain() } }
        return Result.Success(domainTracks)
    }

    private suspend fun fetchApiTracks(localTracksSize: Int): Result<List<Track>, DataError> {
        val allApiTracks = mutableListOf<Track>()
        val maxTracks = AppConstants.Limits.MAX_TRACKS_CACHED
        val limitTracksPerCall = NetworkConstants.Limits.TRACKS_PER_CALL
        var offset = 0

        while (offset < localTracksSize && offset < maxTracks) {
            val result = spotifyApi.getFavoriteTracks(limitTracksPerCall, offset)
            result.onSuccess { favTracksDto ->
                favTracksDto.mapAddedAt()
                val apiTracks = favTracksDto.items.map { it.track.toDomain() }
                allApiTracks.addAll(apiTracks)
                offset += limitTracksPerCall
            }.onError {
                return Result.Error(it)
            }
        }
        return Result.Success(allApiTracks)
    }

    private suspend fun synchronizeLocalTracksWithApi(localTracks: List<TrackEntity>, apiTracks: List<Track>) {
        // Remove local tracks that are not in the API
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
        print(apiTracks)
        println(localTracks)
    }
}
