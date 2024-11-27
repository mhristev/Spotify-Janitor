package org.internship.kmp.martin.track.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.internship.kmp.martin.album.data.database.AlbumEntity
import org.internship.kmp.martin.artist.data.database.ArtistEntity

@Entity
@TypeConverters(Converters::class)
data class TrackEntity (
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val artists: List<ArtistEntity>,
    val album: AlbumEntity,
    val addedAt: LocalDateTime? = null
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
    @TypeConverter
    fun fromArtistList(artists: List<ArtistEntity>?): String? {
        return artists?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toArtistList(data: String?): List<ArtistEntity>? {
        return data?.let { Json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromAlbum(album: AlbumEntity?): String? {
        return album?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toAlbum(data: String?): AlbumEntity? {
        return data?.let { Json.decodeFromString(it) }
    }
}