package org.internship.kmp.martin

import android.app.Application
import org.internship.kmp.martin.di.initKoin
import org.koin.android.ext.koin.androidContext

class SpotifyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@SpotifyApplication)
        }
    }
}