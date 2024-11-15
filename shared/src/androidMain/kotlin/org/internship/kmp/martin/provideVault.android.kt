package org.internship.kmp.martin

import android.content.Context
import com.liftric.kvault.KVault

actual fun provideKVault(context: Any?): KVault {
    require(context is Context) { "Context must be provided for Android" }
    return KVault(context)
}