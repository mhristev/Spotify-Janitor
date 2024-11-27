package org.internship.kmp.martin.album.data.dto

import kotlinx.serialization.Serializable
import org.internship.kmp.martin.spotify_user.data.dto.ImageDto

@Serializable
data class AlbumDto(
    val id: String,
    val name: String,
    val images: List<ImageDto>,
)