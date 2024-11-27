package org.internship.kmp.martin.album.domain

import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: String,
    val name: String,
    val imageUrl: String
)
