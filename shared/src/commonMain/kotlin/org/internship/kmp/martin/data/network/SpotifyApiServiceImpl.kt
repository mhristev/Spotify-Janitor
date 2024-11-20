//package org.internship.kmp.martin.services.impl
//
//import io.ktor.client.HttpClient
//import io.ktor.client.request.get
//import io.ktor.client.request.header
//import io.ktor.client.statement.HttpResponse
//import io.ktor.client.statement.bodyAsText
//import kotlinx.serialization.decodeFromString
//import io.ktor.http.HttpHeaders
//import io.ktor.http.HttpStatusCode
//import kotlinx.serialization.json.Json
//import org.internship.kmp.martin.data.domain.SpotifyUser
//import org.internship.kmp.martin.data.domain.Track
//import org.internship.kmp.martin.services.SpotifyApiService
//import org.internship.kmp.martin.services.TokenManager
//
//class SpotifyApiServiceImpl(private val httpClient: HttpClient, private val tokenManager: TokenManager): SpotifyApiService {
//    override suspend fun getUserProfile(): SpotifyUser? {
//        val token = tokenManager.getToken()
//        val response: HttpResponse = httpClient.get("https://api.spotify.com/v1/me") {
//            header(HttpHeaders.Authorization, "Bearer $token")
//        }
//        return if (response.status == HttpStatusCode.OK) {
//            val a = Json.decodeFromString<SpotifyUser>(response.bodyAsText())
//            return a
//        } else {
//            null
//        }
//    }
//
//    override suspend fun getSavedTracks(): List<Track> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun removeTrackFromSaved(trackId: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun addTrackToFavorites(trackId: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun searchTracks(query: String): List<Track> {
//        TODO("Not yet implemented")
//    }
//}