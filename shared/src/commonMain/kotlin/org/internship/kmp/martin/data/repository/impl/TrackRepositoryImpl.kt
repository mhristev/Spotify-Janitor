package org.internship.kmp.martin.data.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.data.database.track.FavoriteTrackDao
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.data.network.SpotifyApi
import org.internship.kmp.martin.data.repository.TrackRepository
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.core.domain.onError
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.data.dto.mapAddedAt
import org.internship.kmp.martin.data.mappers.toDomain
import org.internship.kmp.martin.data.mappers.toEntity

class TrackRepositoryImpl(private val trackDao: FavoriteTrackDao, private val spotifyApi: SpotifyApi): TrackRepository {

    override suspend fun getFavoriteTracks(): Result<List<Track>, DataError>  {
        var tracks = trackDao.getFavoriteTracks().firstOrNull() ?: emptyList()
//        tracks.forEach { trackDao.removeFavTrack(it.id) }
//        tracks = emptyList()
        if (tracks.isEmpty() || tracks.size < 50) {
            val result = spotifyApi.getFavoriteTracks(50, 0)
            result.onSuccess { favTracksDto ->
                favTracksDto.mapAddedAt()
                favTracksDto.items.forEach { trackDto ->
                    trackDao.upsert(trackDto.track.toDomain().toEntity())
                }
                val domainTracks = trackDao.getFavoriteTracks().toList().flatten().map { it.toDomain() }
                return Result.Success(domainTracks)
            }
                .onError {
                    return Result.Error(it)
                }
        }

        val domainTracks = tracks.map { it.toDomain() }
        return Result.Success(domainTracks)
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
        val result = spotifyApi.getFavoriteTracks(50, 0)
        result.onSuccess { favTracksDto ->

            favTracksDto.mapAddedAt()
            val apiTrackIds = favTracksDto.items.map { it.track.id }
            val apiTracks = favTracksDto.items.map { it.track.toDomain()}
            val localTracks = trackDao.getFavoriteTracks().firstOrNull() ?: emptyList()

            // Remove local tracks that are not in the API
            localTracks.filterNot { apiTrackIds.contains(it.id) }.forEach { trackDao.removeFavTrack(it.id) }

            // Add tracks from the API that are not in the local database
            apiTracks.filterNot { apiTrack -> localTracks.any { it.id == apiTrack.id } }.forEach { track ->
                trackDao.upsert(track.toEntity())
            }
            // update the addedAt factor
            apiTrackIds.filter { id -> localTracks.any { it.id == id } }.forEach { id ->
                val apiTrack = apiTracks.first { it.id == id }
                val localTrack = localTracks.first { it.id == id }
                if (apiTrack.addedAt != localTrack.addedAt) {
                    trackDao.upsert(apiTrack.toEntity())
                }
            }
        }.onError {
            return Result.Error(it)
        }
        val tracks = trackDao.getFavoriteTracks()
        return Result.Success(tracks.map { it.map { entity -> entity.toDomain() } })
    }
}