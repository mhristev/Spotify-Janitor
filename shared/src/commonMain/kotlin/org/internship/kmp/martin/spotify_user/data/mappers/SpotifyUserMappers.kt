package org.internship.kmp.martin.spotify_user.data.mappers

import org.internship.kmp.martin.spotify_user.data.database.SpotifyUserEntity
import org.internship.kmp.martin.spotify_user.data.dto.SpotifyUserDto
import org.internship.kmp.martin.spotify_user.domain.SpotifyUser

fun SpotifyUser.toEntity(): SpotifyUserEntity {
    return SpotifyUserEntity(
        id = this.id,
        country = this.country,
        displayName = this.displayName,
        email = this.email,
        product = this.product,
        imageUrl = this.imageUrl,
        followers = this.followers
    )
}

fun SpotifyUserDto.toDomain(): SpotifyUser {
    return SpotifyUser(
        id = this.id,
        displayName = this.displayName,
        email = this.email,
        country = this.country,
        product = this.product,
        followers = this.followers.total,
        imageUrl = this.images.first().url
    )
}
