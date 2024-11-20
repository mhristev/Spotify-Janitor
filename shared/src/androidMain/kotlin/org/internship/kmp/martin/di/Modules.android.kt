package org.internship.kmp.martin.di

import io.ktor.client.engine.HttpClientEngine


actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single { DatabaseFactory(androidApplication()) }
    }