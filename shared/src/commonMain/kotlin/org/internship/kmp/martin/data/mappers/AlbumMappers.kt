package org.internship.kmp.martin.data.mappers

import org.internship.kmp.martin.data.database.album.AlbumEntity
import org.internship.kmp.martin.data.domain.Album
import org.internship.kmp.martin.data.dto.AlbumDto


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