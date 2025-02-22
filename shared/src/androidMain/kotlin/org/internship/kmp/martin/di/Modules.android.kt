package org.internship.kmp.martin.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.internship.kmp.martin.core.data.auth.KVaultFactory
import org.internship.kmp.martin.core.data.database.DatabaseFactory
import org.internship.kmp.martin.core.presentation.AuthViewModel
import org.internship.kmp.martin.track.presentation.browse_tracks.BrowseTracksViewModel
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.internship.kmp.martin.core.presentation.LoginViewModel
import org.internship.kmp.martin.spotify_user.presentation.UserProfileViewModel
import org.internship.kmp.martin.track.data.repository.TrackRepository
import org.internship.kmp.martin.track.domain.TrackRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.android.ext.koin.androidApplication


actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single { DatabaseFactory(androidApplication()) }
        single { KVaultFactory(androidApplication()) }

        factory { UserProfileViewModel(get(), get()) }
        factory { LoginViewModel(get()) }
        single { FavoriteTracksViewModel(get()) }
        factory { BrowseTracksViewModel(get()) }
        single { AuthViewModel(get()) }
    }