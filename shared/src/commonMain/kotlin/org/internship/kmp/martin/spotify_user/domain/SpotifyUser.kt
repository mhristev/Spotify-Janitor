package org.internship.kmp.martin.spotify_user.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class SpotifyUser(
    val id: String,
    val displayName: String,
    val email: String,
    val country: String,
    val product: String,
    val followers: Int,
    val imageUrl: String
)
