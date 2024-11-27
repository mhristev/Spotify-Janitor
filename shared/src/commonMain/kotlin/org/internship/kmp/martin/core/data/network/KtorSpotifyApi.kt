package org.internship.kmp.martin.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.http.HttpHeaders
import org.internship.kmp.martin.core.data.network.client.safeCall
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.core.data.auth.AuthManager
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.spotify_user.data.dto.SpotifyUserDto
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.track.data.dto.FavoriteTracksDto
import org.internship.kmp.martin.track.data.dto.SearchResponseDto

class KtorSpotifyApi(private val httpClient: HttpClient, private val auth: AuthManager):
    SpotifyApi {
    override fun setToken(accessToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun login(accessToken: String): Result<SpotifyUserDto, DataError.Remote>  {
        auth.login(accessToken)
        return safeCall<SpotifyUserDto> {
            httpClient.get("https://api.spotify.com/v1/me") {
                header(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }
    }

    override suspend fun getFavoriteTracks(limit: Int, offset: Int): Result<FavoriteTracksDto, DataError.Remote> {
        val accessToken = auth.getAccessToken()
        return safeCall<FavoriteTracksDto> {
            httpClient.get("https://api.spotify.com/v1/me/tracks") {
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }
    }

override suspend fun addFavoriteTrack(track: Track): Result<Unit, DataError.Remote> {
    val accessToken = auth.getAccessToken()
    return safeCall<Unit> {
        httpClient.put("https://api.spotify.com/v1/me/tracks") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("ids", track.id)
        }
    }
}

override suspend fun removeFavoriteTrack(track: Track): Result<Unit, DataError.Remote> {
    val accessToken = auth.getAccessToken()
    return safeCall<Unit> {
        httpClient.delete("https://api.spotify.com/v1/me/tracks") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("ids", track.id)
        }
    }
}

    override suspend fun searchTracksInSpotify(query: String): Result<SearchResponseDto, DataError.Remote> {
        val accessToken = auth.getAccessToken()
        return safeCall<SearchResponseDto> {
            httpClient.get("https://api.spotify.com/v1/search") {
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                parameter("q", query)
                parameter("type", "track")
                parameter("limit", 50)
            }
        }
    }

}