package org.internship.kmp.martin.data.auth

import com.liftric.kvault.KVault

actual class KVaultFactory {
    actual fun create(): KVault {
        return KVault()
    }
}