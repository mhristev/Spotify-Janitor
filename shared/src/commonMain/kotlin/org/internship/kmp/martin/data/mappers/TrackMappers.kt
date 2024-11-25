package org.internship.kmp.martin.data.mappers

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.internship.kmp.martin.data.database.track.TrackEntity
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.data.dto.TrackDto

fun TrackDto.toDomain(): Track {
    return Track(
        id = this.id,
        name = this.name,
        artists = this.artists.map { it.toDomain() },
        album = this.album.toDomain(),
        addedAt = this.added_at?.let { Instant.parse(it).toLocalDateTime(TimeZone.UTC) }
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        id = this.id,
        name = this.name,
        artists = this.artists.map { it.toEntity() },
        album = this.album.toEntity(),
        addedAt = this.addedAt
    )
}

fun TrackEntity.toDomain(): Track {
    return Track(
        id = this.id,
        name = this.name,
        artists = this.artists.map { it.toDomain() },
        album = this.album.toDomain(),
        addedAt = this.addedAt
    )
}