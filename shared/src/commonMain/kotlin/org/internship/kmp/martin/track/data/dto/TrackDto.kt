package org.internship.kmp.martin.track.data.dto

import kotlinx.serialization.Serializable
import org.internship.kmp.martin.album.data.dto.AlbumDto
import org.internship.kmp.martin.artist.data.dto.ArtistDto
import org.internship.kmp.martin.spotify_user.data.dto.ImageDto

@Serializable
data class TrackDto(
    val id: String,
    val name: String,
    val album: AlbumDto,
    val artists: List<ArtistDto>,
    var added_at: String? = null,
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