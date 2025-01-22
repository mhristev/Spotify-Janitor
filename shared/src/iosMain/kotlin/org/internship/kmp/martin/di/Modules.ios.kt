package org.internship.kmp.martin.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.internship.kmp.martin.core.data.auth.KVaultFactory
import org.internship.kmp.martin.core.data.database.DatabaseFactory
import org.internship.kmp.martin.core.presentation.AuthViewModel
import org.internship.kmp.martin.core.presentation.LoginViewModel
import org.internship.kmp.martin.spotify_user.presentation.UserProfileViewModel
import org.internship.kmp.martin.track.presentation.browse_tracks.BrowseTracksViewModel
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { Darwin.create() }
        single { DatabaseFactory() }
        single { KVaultFactory() }


        single { FavoriteTracksViewModel(get()) }
        factory { AuthViewModel(get()) }
        factory {UserProfileViewModel(get(), get())}
        factory { BrowseTracksViewModel(get()) }
    }