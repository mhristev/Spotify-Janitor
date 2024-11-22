package org.internship.kmp.martin.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import org.internship.kmp.martin.core.data.safeCall
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.data.auth.AuthManager
import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserDao
import org.internship.kmp.martin.data.database.track.FavoriteTrackDao
import org.internship.kmp.martin.data.domain.SpotifyUser
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.data.dto.SpotifyUserDto
import org.internship.kmp.martin.data.dto.TrackDto
import org.internship.kmp.martin.core.domain.Result

//class KtorSpotifyApi(private val httpClient: HttpClient): SpotifyApi {
//
//    override suspend fun getFavouriteTracls(): String {
//        return safeCall<GetFavouriteTracksresponseDto> {
//                httpClient.get("https://api.spotify.com/v1/me/top/tracks")
//                {
//                    header("Authorization")
//                    parameter("time_range", "short_term")
//                }
//            }
//        }
//
//    }
//}

class KtorSpotifyApi(private val httpClient: HttpClient, private val auth: AuthManager): SpotifyApi {
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


    override fun getFavoriteTracks(): List<TrackDto> {
        TODO("Not yet implemented")
    }

    override fun addFavoriteTrack(track: Track) {
        TODO("Not yet implemented")
    }

    override fun removeFavoriteTrack(track: Track) {
        TODO("Not yet implemented")
    }

}