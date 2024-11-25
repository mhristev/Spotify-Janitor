package org.internship.kmp.martin.data.database.artist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: ArtistEntity)

    @Query("SELECT * FROM ArtistEntity WHERE id = :id")
    suspend fun getArtistById(id: String): ArtistEntity?
}