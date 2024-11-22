package org.internship.kmp.martin.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(private val context: Context) {

    actual fun createFavTracksDatabase(): RoomDatabase.Builder<RoomAppDatabase> {
        val appContext = context.applicationContext
        val dbFileFavTracks = appContext.getDatabasePath(RoomAppDatabase.DB_NAME_FAV_TRACK)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFileFavTracks.absolutePath
        )
    }

    actual fun createSpotifyUserDatabase(): RoomDatabase.Builder<RoomAppDatabase> {
        val appContext = context.applicationContext
        val dbFileSpUser = appContext.getDatabasePath(RoomAppDatabase.DB_NAME_SPOTIFY_USER)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFileSpUser.absolutePath
        )
    }
}