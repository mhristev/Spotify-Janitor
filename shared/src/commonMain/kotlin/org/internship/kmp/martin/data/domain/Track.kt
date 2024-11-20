package org.internship.kmp.martin.data.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class Track(
    val id: String,
    val name: String,
    val artists: List<String>,
    val isFavorite: Boolean = false,
    val albumArtUrl: String
) {
    companion object {
        fun fromJson(jsonString: String): Track {
            val json = Json { ignoreUnknownKeys = true }
            val jsonObject = json.parseToJsonElement(jsonString).jsonObject
            val id = jsonObject["id"]?.jsonPrimitive?.contentOrNull ?: ""
            val name = jsonObject["name"]?.jsonPrimitive?.contentOrNull ?: ""
            val artists = jsonObject["artists"]?.jsonArray?.map { it.jsonObject["name"]?.jsonPrimitive?.contentOrNull ?: "" } ?: emptyList()
            val albumArtUrl = jsonObject["images"]?.jsonArray?.firstOrNull()?.jsonObject?.get("url")?.jsonPrimitive?.contentOrNull ?: ""
            return Track(id, name, artists, albumArtUrl = albumArtUrl)
        }
    }
}



