package org.internship.kmp.martin.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.internship.kmp.martin.core.data.auth.KVaultFactory
import org.internship.kmp.martin.core.data.database.DatabaseFactory
import org.internship.kmp.martin.track.presentation.BrowseTracksViewModel
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.internship.kmp.martin.core.presentation.LaunchScreenViewModel
import org.internship.kmp.martin.spotify_user.presentation.UserViewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.android.ext.koin.androidApplication


actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single { DatabaseFactory(androidApplication()) }
        single { KVaultFactory(androidApplication()) }

        factory { UserViewModel(get()) }
        factory { LaunchScreenViewModel(get()) }
        factory { FavoriteTracksViewModel(get()) }
        factory { BrowseTracksViewModel(get()) }
    }