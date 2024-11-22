package org.internship.kmp.martin.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.internship.kmp.martin.core.data.HttpClientFactory
import org.internship.kmp.martin.data.auth.AuthManager
import org.internship.kmp.martin.data.auth.KVaultAuthManager
import org.internship.kmp.martin.data.auth.KVaultFactory
import org.internship.kmp.martin.data.database.DatabaseFactory
import org.internship.kmp.martin.data.database.RoomAppDatabase
import org.internship.kmp.martin.data.network.KtorSpotifyApi
import org.internship.kmp.martin.data.network.SpotifyApi
import org.internship.kmp.martin.data.repository.SpotifyUserRepository
import org.internship.kmp.martin.data.repository.impl.SpotifyUserRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    single { KVaultAuthManager(get<KVaultFactory>().create()) }
    single<AuthManager> { KVaultAuthManager(get()) }
    single<SpotifyApi> { KtorSpotifyApi(get(), get()) }


    single<SpotifyUserRepository> { SpotifyUserRepositoryImpl(get(), get()) }
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
        get<KVaultFactory>().create()
    }

    single { get<RoomAppDatabase>().spotifyUserDao }
    single { get<RoomAppDatabase>().favoriteTrackDao }
}

