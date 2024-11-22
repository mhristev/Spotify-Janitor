package org.internship.kmp.martin.data.repository.impl

import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.data.database.track.FavoriteTrackDao
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.data.network.SpotifyApi
import org.internship.kmp.martin.data.repository.TrackRepository
import org.internship.kmp.martin.data.repository.images.ImageManager

class TrackRepositoryImpl(trackDao: FavoriteTrackDao, spotifyApi: SpotifyApi): TrackRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        TODO("Not yet implemented")
    }

    override fun addFavoriteTrack(track: Track) {
        TODO("Not yet implemented")
    }

    override fun removeFavoriteTrack(track: Track) {
        TODO("Not yet implemented")
    }

    override fun searchTracks(query: String): Flow<List<String>> {
        TODO("Not yet implemented")
    }
}