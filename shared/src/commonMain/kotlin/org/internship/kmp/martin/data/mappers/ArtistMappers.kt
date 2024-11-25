package org.internship.kmp.martin.data.mappers

import org.internship.kmp.martin.data.database.artist.ArtistEntity
import org.internship.kmp.martin.data.domain.Artist
import org.internship.kmp.martin.data.dto.ArtistDto


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