package org.internship.kmp.martin.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import org.internship.kmp.martin.data.database.album.AlbumDao
import org.internship.kmp.martin.data.database.album.AlbumEntity
import org.internship.kmp.martin.data.database.artist.ArtistDao
import org.internship.kmp.martin.data.database.artist.ArtistEntity
import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserDao
import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserEntity
import org.internship.kmp.martin.data.database.track.Converters
import org.internship.kmp.martin.data.database.track.FavoriteTrackDao
import org.internship.kmp.martin.data.database.track.TrackEntity


@Database(
    entities = [SpotifyUserEntity::class, TrackEntity::class, AlbumEntity::class, ArtistEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class RoomAppDatabase: RoomDatabase() {
    abstract val favoriteTrackDao: FavoriteTrackDao
    abstract val spotifyUserDao: SpotifyUserDao
    abstract val albumDao: AlbumDao
    abstract val artistDao: ArtistDao

    companion object {
        const val DB_NAME_FAV_TRACK = "fav_track.db"
        const val DB_NAME_SPOTIFY_USER = "spotify_user.db"
        const val DB_NAME_ALBUM = "album.db"
        const val DB_NAME_ARTIST = "artist.db"
    }
}