package org.internship.kmp.martin.track.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
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
import org.internship.kmp.martin.spotify_user.data.mappers.toDomain
import org.internship.kmp.martin.spotify_user.data.mappers.toEntity
import org.internship.kmp.martin.track.data.mappers.toDomain
import org.internship.kmp.martin.track.data.mappers.toEntity

class TrackRepositoryImpl(private val trackDao: FavoriteTrackDao, private val spotifyApi: SpotifyApi):
    TrackRepository {


    override suspend fun getFavoriteTracks(): Result<List<Track>, DataError> {
//        private var currentOffset = 0
        val tracks = trackDao.getFavoriteTracks().firstOrNull() ?: emptyList()

        if (tracks.isEmpty() || tracks.size < 50) {
            val result = spotifyApi.getFavoriteTracks(50, 0)
            result.onSuccess { favTracksDto ->
                favTracksDto.mapAddedAt()
                favTracksDto.items.forEach { trackDto ->
                    trackDao.upsert(trackDto.track.toDomain().toEntity())
                }
//                currentOffset += favTracksDto.items.size // Update the offset

                val domainTracks = trackDao.getFavoriteTracks().toList().flatten().map { it.toDomain() }.take(50)
                return Result.Success(domainTracks)
            }.onError {
                return Result.Error(it)
            }
        }

        // If tracks are already present, map them to domain
        val domainTracks = tracks.take(50).map { it.toDomain() }

        // Return the tracks from the local DB
        return Result.Success(domainTracks)
    }

    override suspend fun getNextFavoriteTracks(currentTrackOffset: Int): Result<List<Track>, DataError> {
        val localTracks = trackDao.getFavoriteTracks().firstOrNull() ?: emptyList()
        if (localTracks.size > currentTrackOffset) {
            val domainTracks = localTracks.drop(currentTrackOffset).take(50).map { it.toDomain() }
            return Result.Success(domainTracks)
        }

        val result = spotifyApi.getFavoriteTracks(50, currentTrackOffset)
        result.onSuccess { favTracksDto ->
            favTracksDto.mapAddedAt()
            favTracksDto.items.forEach { trackDto ->
                trackDao.upsert(trackDto.track.toDomain().toEntity())
            }
            val tracks = trackDao.getFavoriteTracks().firstOrNull() ?: emptyList()
            val domainTracks = tracks.drop(currentTrackOffset).take(50).map { it.toDomain() }
            return Result.Success(domainTracks)
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

    override suspend fun removeFavoriteTrack(track: Track): Result<Unit, DataError> {
        trackDao.removeFavTrack(track.id)
        spotifyApi.removeFavoriteTrack(track)
            .onError {
                return Result.Error(it)
            }
        return Result.Success(Unit)
    }

    override suspend fun searchTracks(query: String): Result<List<Track>, DataError> {
       var result = spotifyApi.searchTracksInSpotify(query)
        result.onSuccess { searchResponseDto ->
            val tracks = searchResponseDto.tracks.items.map { it.toDomain() }
            return Result.Success(tracks)
        }.onError {
            return Result.Error(it)
        }
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun synchronizeTracks(): Result<Flow<List<Track>>, DataError> {
        var offset = 0
        val localTracks = trackDao.getFavoriteTracks().firstOrNull() ?: emptyList()
        var totalTracksFetched = 0

        val maxTracks = AppConstants.Limits.MAX_TRACKS_CACHED
        val limitTracksPerCall = NetworkConstants.Limits.TRACKS_PER_CALL

        val allApiTracks = mutableListOf<Track>()

        // Fetch tracks in batches of 50, until the local DB has enough tracks (but no more than 1000)
        while (totalTracksFetched < localTracks.size && totalTracksFetched < maxTracks) {
            val result = spotifyApi.getFavoriteTracks(limitTracksPerCall, offset)
            result.onSuccess { favTracksDto ->
                favTracksDto.mapAddedAt()

                val apiTracks = favTracksDto.items.map { it.track.toDomain() }

                totalTracksFetched += 50
                // Collect the API tracks
                allApiTracks.addAll(apiTracks)

                // Increase the offset for the next batch of tracks
                offset += 50
            }.onError {
                return Result.Error(it)
            }
        }

        // Remove local tracks that are not in the API
        localTracks.filterNot { localTrack -> allApiTracks.any { it.id == localTrack.id } }.forEach { track ->
            trackDao.removeFavTrack(track.id)
        }

        // Add tracks from the API that are not in the local database
        allApiTracks.filterNot { apiTrack -> localTracks.any { it.id == apiTrack.id } }.forEach { track ->
            trackDao.upsert(track.toEntity())
        }

        // Update the addedAt factor for tracks that exist in both the local DB and API
        allApiTracks.filter { track -> localTracks.any { it.id == track.id } }.forEach { track ->
            val localTrack = localTracks.first { it.id == track.id }
            if (track.addedAt != localTrack.addedAt) {
                trackDao.upsert(track.toEntity())
            }
        }

        val domainTracks = trackDao.getFavoriteTracks().map { it.map { trackEntity -> trackEntity.toDomain()  } }
        return Result.Success(domainTracks)
    }
}