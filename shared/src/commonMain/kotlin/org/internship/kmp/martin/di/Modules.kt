package org.internship.kmp.martin.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.internship.kmp.martin.core.data.network.client.HttpClientFactory
import org.internship.kmp.martin.core.data.auth.AuthManager
import org.internship.kmp.martin.core.data.auth.KVaultAuthManager
import org.internship.kmp.martin.core.data.auth.KVaultFactory
import org.internship.kmp.martin.core.data.database.AuthRepository
import org.internship.kmp.martin.core.data.database.AuthRepositoryImpl
import org.internship.kmp.martin.core.data.database.DatabaseFactory
import org.internship.kmp.martin.core.data.database.RoomAppDatabase
import org.internship.kmp.martin.core.data.network.KtorSpotifyApi
import org.internship.kmp.martin.core.data.network.SpotifyApi
import org.internship.kmp.martin.spotify_user.data.repository.SpotifyUserRepository
import org.internship.kmp.martin.spotify_user.domain.SpotifyUserRepositoryImpl
import org.internship.kmp.martin.track.data.repository.TrackRepository
import org.internship.kmp.martin.track.domain.TrackRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    single<AuthManager> { KVaultAuthManager(get<KVaultFactory>().create()) }
    single<MyTestInterface> { MyTestInterfaceImpl(get()) }

    single<SpotifyUserRepository> { SpotifyUserRepositoryImpl(get(), get()) }

    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get()) }
    single {
        get<DatabaseFactory>().createFavTracksDatabase()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single {
        get<DatabaseFactory>().createSpotifyUserDatabase()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single {
        get<DatabaseFactory>().createAlbumDatabase()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single {
        get<DatabaseFactory>().createArtistDatabase()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single { get<RoomAppDatabase>().spotifyUserDao }
    single { get<RoomAppDatabase>().favoriteTrackDao }
    single<TrackRepository> { TrackRepositoryImpl(get(), get()) }
    single<SpotifyApi> { KtorSpotifyApi(get(), get()) }
}

