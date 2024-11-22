package org.internship.kmp.martin.data.database.track

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

    @Query("DELETE FROM TrackEntity WHERE id = :id")
    suspend fun deleteFavoriteBook(id: String)
}