package org.internship.kmp.martin.data.network

import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserDao
import org.internship.kmp.martin.data.database.track.FavoriteTrackDao
import org.internship.kmp.martin.data.domain.Track

interface SpotifyApi {
    fun setToken(accessToken: String)
    fun getSpotifyUserProfile(): SpotifyUserDao
    fun getFavoriteTracks(): List<FavoriteTrackDao>
    fun addFavoriteTrack(track: Track)
    fun removeFavoriteTrack(track: Track)
}