package org.internship.kmp.martin.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.internship.kmp.martin.album.data.database.AlbumDao
import org.internship.kmp.martin.album.data.database.AlbumEntity
import org.internship.kmp.martin.artist.data.database.ArtistDao
import org.internship.kmp.martin.artist.data.database.ArtistEntity
import org.internship.kmp.martin.spotify_user.data.database.SpotifyUserDao
import org.internship.kmp.martin.spotify_user.data.database.SpotifyUserEntity
import org.internship.kmp.martin.track.data.database.Converters
import org.internship.kmp.martin.track.data.database.FavoriteTrackDao
import org.internship.kmp.martin.track.data.database.TrackEntity

@Database(
    entities = [SpotifyUserEntity::class, TrackEntity::class, AlbumEntity::class, ArtistEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class RoomAppDatabase: RoomDatabase() {
    abstract val favoriteTrackDao: FavoriteTrackDao
    abstract val spotifyUserDao: SpotifyUserDao

    companion object {
        const val DB_NAME_FAV_TRACK = "fav_track.db"
        const val DB_NAME_SPOTIFY_USER = "spotify_user.db"
        const val DB_NAME_ALBUM = "album.db"
        const val DB_NAME_ARTIST = "artist.db"
    }
}