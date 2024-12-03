package org.internship.kmp.martin.di

import io.ktor.client.HttpClient
import org.internship.kmp.martin.core.data.auth.AuthManager
import org.internship.kmp.martin.core.data.network.SpotifyApi
import org.internship.kmp.martin.track.data.database.FavoriteTrackDao

class MyTestInterfaceImpl(private val auth: AuthManager): MyTestInterface {
    override fun sayHello(): String {
        auth.toString()
        return "Hello from Ivan"
    }
}