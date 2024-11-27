package org.internship.kmp.martin.album.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: AlbumEntity)

    @Query("SELECT * FROM AlbumEntity WHERE id = :id")
    suspend fun getAlbumById(id: String): AlbumEntity?
}