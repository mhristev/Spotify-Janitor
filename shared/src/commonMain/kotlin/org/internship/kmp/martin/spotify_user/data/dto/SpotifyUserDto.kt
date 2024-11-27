package org.internship.kmp.martin.spotify_user.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyUserDto (
    val id: String,
    @SerialName("display_name") val displayName: String,
    val email: String,
    val country: String,
    val product: String,
    val followers: FollowersDto,
    val images: List<ImageDto>
)


@Serializable
data class ImageDto (
    val url: String = "",
    private val  height: Int = 0,
    private val width: Int = 0
)


@Serializable
data class FollowersDto (
    private val href: String? = "",
    val total: Int = 0
)


