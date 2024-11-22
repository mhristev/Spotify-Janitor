package org.internship.kmp.martin.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserDao
import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserEntity
import org.internship.kmp.martin.data.database.track.FavoriteTrackDao
import org.internship.kmp.martin.data.database.track.TrackEntity


@Database(
    entities = [SpotifyUserEntity::class, TrackEntity::class],
    version = 1
)

@ConstructedBy(AppDatabaseConstructor::class)
abstract class RoomAppDatabase: RoomDatabase() {
    abstract val favoriteTrackDao: FavoriteTrackDao
    abstract val spotifyUserDao: SpotifyUserDao

    companion object {
        const val DB_NAME_FAV_TRACK = "fav_track.db"
        const val DB_NAME_SPOTIFY_USER = "spotify_user.db"
    }
}