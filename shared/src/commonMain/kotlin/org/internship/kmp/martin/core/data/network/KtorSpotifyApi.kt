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
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.spotify_user.data.dto.SpotifyUserDto
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.core.domain.onError
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.track.data.dto.FavoriteTracksDto
import org.internship.kmp.martin.track.data.dto.SearchResponseDto

class KtorSpotifyApi(private val httpClient: HttpClient, private val auth: AuthManager) :
    SpotifyApi {

    override suspend fun getCurrentUser(): Result<SpotifyUserDto, DataError>  {
        auth.getValidAccessToken()
            .onSuccess { accessToken ->
                return safeCall<SpotifyUserDto> {
                    httpClient.get("${AppConstants.SpotifyApi.BASE_URL}/me") {
                        header(HttpHeaders.Authorization, "Bearer $accessToken")
                    }
                }
            }
            .onError {
                return Result.Error(it)
            }
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun getFavoriteTracks(limit: Int, offset: Int): Result<FavoriteTracksDto, DataError> {
       auth.getValidAccessToken()
            .onSuccess { accessToken ->
                if (limit > AppConstants.SpotifyApi.MAX_TRACKS_PER_CALL) return Result.Error(DataError.Remote.LIMIT_EXCEEDED)
                return safeCall<FavoriteTracksDto> {
                    httpClient.get("${AppConstants.SpotifyApi.BASE_URL}/me/tracks") {
                        header(HttpHeaders.Authorization, "Bearer $accessToken")
                        parameter("limit", limit)
                        parameter("offset", offset)
                    }
                }
            }
            .onError {
                return Result.Error(it)
            }
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun addFavoriteTrack(track: Track): Result<Unit, DataError> {
        auth.getValidAccessToken()
            .onSuccess { accessToken ->
                return safeCall<Unit> {
                    httpClient.put("${AppConstants.SpotifyApi.BASE_URL}/me/tracks") {
                        header(HttpHeaders.Authorization, "Bearer $accessToken")
                        parameter("ids", track.id)
                    }
                }
            }
            .onError {
                return Result.Error(it)
            }
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun removeFavoriteTrack(track: Track): Result<Unit, DataError> {
        auth.getValidAccessToken()
            .onSuccess { accessToken ->
                return safeCall<Unit> {
                    httpClient.delete("${AppConstants.SpotifyApi.BASE_URL}/me/tracks") {
                        header(HttpHeaders.Authorization, "Bearer $accessToken")
                        parameter("ids", track.id)
                    }
                }
            }
            .onError {
                return Result.Error(it)
            }
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun searchTracksInSpotify(query: String): Result<SearchResponseDto, DataError> {
        auth.getValidAccessToken()
            .onSuccess { accessToken ->
                return safeCall<SearchResponseDto> {
                    httpClient.get("${AppConstants.SpotifyApi.BASE_URL}/search") {
                        header(HttpHeaders.Authorization, "Bearer $accessToken")
                        parameter("q", query)
                        parameter("type", "track")
                        parameter("limit", 50)
                    }
                }
            }
            .onError {
                return Result.Error(it)
            }
        return Result.Error(DataError.Remote.UNKNOWN)
    }
}