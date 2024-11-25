package org.internship.kmp.martin.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
    val id: String,
    val name: String,
    val album: AlbumDto,
    val artists: List<ArtistDto>,
    var added_at: String? = null,
)

@Serializable
data class AlbumDto(
    val id: String,
    val name: String,
    val images: List<ImageDto>,
)

@Serializable
data class ArtistDto(
    val id: String,
    val name: String,
)

@Serializable
data class FavoriteTracksDto(
    var items: List<FavoriteItemsDto>
)

@Serializable
data class FavoriteItemsDto(
    var added_at: String,
    val track: TrackDto
)

fun FavoriteTracksDto.mapAddedAt() {
    this.items.map { it.track.added_at = it.added_at}
}


@Serializable
data class SearchResponseDto(
    var tracks: SeachResponseTracks,
)

@Serializable
data class SeachResponseTracks(
    var items: List<TrackDto>
)