package org.internship.kmp.martin.core.data.auth

import android.content.Context
import com.liftric.kvault.KVault

actual class KVaultFactory(private val context: Context) {
    actual fun create(): KVault {
        return KVault(context)
    }
}