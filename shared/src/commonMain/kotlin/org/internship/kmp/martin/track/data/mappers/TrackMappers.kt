package org.internship.kmp.martin.track.data.mappers

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.internship.kmp.martin.album.data.mappers.toDomain
import org.internship.kmp.martin.album.data.mappers.toEntity
import org.internship.kmp.martin.track.data.database.TrackEntity
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.artist.data.mappers.toDomain
import org.internship.kmp.martin.album.data.mappers.toDomain
import org.internship.kmp.martin.artist.data.mappers.toEntity
import org.internship.kmp.martin.track.data.dto.TrackDto
import org.internship.kmp.martin.track.data.mappers.toDomain

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
        addedAt = this.addedAt ?: Clock.System.now().toLocalDateTime(TimeZone.UTC)

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