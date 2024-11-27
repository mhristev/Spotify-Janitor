package org.internship.kmp.martin.track.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrackDao {
    @Upsert
    suspend fun upsert(track: TrackEntity)

    @Query("SELECT * FROM TrackEntity")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM TrackEntity")
    suspend fun getFavoriteTracksHandle(): List<TrackEntity>

    @Query("DELETE FROM TrackEntity WHERE id = :id")
    suspend fun removeFavTrack(id: String)

    @Upsert
    suspend fun insertAll(tracks: List<TrackEntity>)
}