package org.internship.kmp.martin.track.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.internship.kmp.martin.album.domain.Album
import org.internship.kmp.martin.artist.domain.Artist

@Serializable
data class Track(
    val id: String,
    val name: String,
    val artists: List<Artist>,
    val album: Album,
    val addedAt: LocalDateTime? = null
)



