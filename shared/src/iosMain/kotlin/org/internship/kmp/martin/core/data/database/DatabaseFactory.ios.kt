package org.internship.kmp.martin.core.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        return requireNotNull(documentDirectory?.path)
    }

    actual fun createFavTracksDatabase(): RoomDatabase.Builder<RoomAppDatabase> {
        val dbFileFavTracks = documentDirectory() + "/${RoomAppDatabase.DB_NAME_FAV_TRACK}"
        return Room.databaseBuilder<RoomAppDatabase>(
            name = dbFileFavTracks
        )
    }

    actual fun createSpotifyUserDatabase(): RoomDatabase.Builder<RoomAppDatabase> {
        val dbSpUser = documentDirectory() + "/${RoomAppDatabase.DB_NAME_SPOTIFY_USER}"
        return Room.databaseBuilder<RoomAppDatabase>(
            name = dbSpUser
        )
    }

    actual fun createAlbumDatabase(): RoomDatabase.Builder<RoomAppDatabase> {
        val db = documentDirectory() + "/${RoomAppDatabase.DB_NAME_ALBUM}"
        return Room.databaseBuilder<RoomAppDatabase>(
            name = db
        )
    }

    actual fun createArtistDatabase(): RoomDatabase.Builder<RoomAppDatabase> {
        val db = documentDirectory() + "/${RoomAppDatabase.DB_NAME_ARTIST}"
        return Room.databaseBuilder<RoomAppDatabase>(
            name = db
        )
    }
}