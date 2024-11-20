package org.internship.kmp.martin.data.network

import io.ktor.client.HttpClient
import org.internship.kmp.martin.core.data.safeCall
import org.internship.kmp.martin.data.auth.AuthManager
import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserDao
import org.internship.kmp.martin.data.database.track.FavoriteTrackDao
import org.internship.kmp.martin.data.domain.Track

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

class KtorSpotifyApi(private val httpClient: HttpClient, private val authManager: AuthManager): SpotifyApi {
    override fun setToken(accessToken: String) {
        TODO("Not yet implemented")
    }

    override fun getSpotifyUserProfile(): SpotifyUserDao {
        TODO("Not yet implemented")
    }

    override fun getFavoriteTracks(): List<FavoriteTrackDao> {
        TODO("Not yet implemented")
    }

    override fun addFavoriteTrack(track: Track) {
        TODO("Not yet implemented")
    }

    override fun removeFavoriteTrack(track: Track) {
        TODO("Not yet implemented")
    }

}