package org.internship.kmp.martin.artist.data.mappers

import org.internship.kmp.martin.artist.data.database.ArtistEntity
import org.internship.kmp.martin.artist.data.dto.ArtistDto
import org.internship.kmp.martin.artist.domain.Artist


fun ArtistEntity.toDomain(): Artist {
    return Artist(
        id = this.id,
        name = this.name
    )
}

fun ArtistDto.toDomain(): Artist {
    return Artist(
        id = this.id,
        name = this.name
    )
}

fun Artist.toEntity(): ArtistEntity {
    return ArtistEntity(
        id = this.id,
        name = this.name
    )
}