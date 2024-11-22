package org.internship.kmp.martin.data.database.spotifyuser

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.data.database.track.TrackEntity

@Dao
interface SpotifyUserDao {
    @Upsert
    suspend fun upsert(user: SpotifyUserEntity)

    @Query("SELECT * FROM SpotifyUserEntity")
    fun getAllUsers(): Flow<List<SpotifyUserEntity>>

}