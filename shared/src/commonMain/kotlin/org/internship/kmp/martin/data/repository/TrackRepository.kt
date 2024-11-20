package org.internship.kmp.martin.data.repository

import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.data.domain.Track

interface TrackRepository {
    fun getFavoriteTracks(): Flow<List<Track>>
    fun addFavoriteTrack(track: Track)
    fun removeFavoriteTrack(track: Track)
    fun searchTracks(query: String) : Flow<List<String>>
}