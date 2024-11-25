package org.internship.kmp.martin.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun createFavTracksDatabase(): RoomDatabase.Builder<RoomAppDatabase>
    fun createSpotifyUserDatabase(): RoomDatabase.Builder<RoomAppDatabase>
    fun createAlbumDatabase(): RoomDatabase.Builder<RoomAppDatabase>
    fun createArtistDatabase(): RoomDatabase.Builder<RoomAppDatabase>
}