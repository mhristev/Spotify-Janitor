package org.internship.kmp.martin.album.data.mappers

import org.internship.kmp.martin.album.data.database.AlbumEntity
import org.internship.kmp.martin.album.data.dto.AlbumDto
import org.internship.kmp.martin.album.domain.Album


fun AlbumEntity.toDomain(): Album {
    return Album(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl
    )
}

fun AlbumDto.toDomain(): Album {
    return Album(
        id = this.id,
        name = this.name,
        imageUrl = this.images.firstOrNull()?.url ?: ""
    )
}

fun Album.toEntity(): AlbumEntity {
    return AlbumEntity(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl
    )
}