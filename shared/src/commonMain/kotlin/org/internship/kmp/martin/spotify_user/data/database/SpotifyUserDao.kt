package org.internship.kmp.martin.spotify_user.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotifyUserDao {
    @Upsert
    suspend fun upsert(user: SpotifyUserEntity)

    @Query("SELECT * FROM SpotifyUserEntity")
    fun getAllUsers(): Flow<List<SpotifyUserEntity>>

    @Query("SELECT * FROM SpotifyUserEntity WHERE id = :userId")
    suspend fun getUserById(userId: String): SpotifyUserEntity?

}