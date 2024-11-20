package org.internship.kmp.martin.data.domain

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
) {
    companion object {
        fun fromJson(jsonString: String): SpotifyUser {
            val json = Json { ignoreUnknownKeys = true }
            val jsonObject = json.parseToJsonElement(jsonString).jsonObject
            val id = jsonObject["id"]?.jsonPrimitive?.contentOrNull ?: ""
            val displayName = jsonObject["display_name"]?.jsonPrimitive?.contentOrNull ?: ""
            val email = jsonObject["email"]?.jsonPrimitive?.contentOrNull ?: ""
            val country = jsonObject["country"]?.jsonPrimitive?.contentOrNull ?: ""
            val product = jsonObject["product"]?.jsonPrimitive?.contentOrNull ?: ""
            val followers = jsonObject["followers"]?.jsonObject?.get("total")?.jsonPrimitive?.int ?: 0
            val imageUrl = jsonObject["images"]?.jsonArray?.firstOrNull()?.jsonObject?.get("url")?.jsonPrimitive?.contentOrNull ?: ""
            return SpotifyUser(id, displayName, email, country, product, followers, imageUrl)
        }
    }
}
