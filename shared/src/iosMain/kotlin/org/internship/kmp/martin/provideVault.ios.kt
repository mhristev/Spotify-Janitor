package org.internship.kmp.martin

import com.liftric.kvault.KVault

actual fun provideKVault(context: Any?): KVault {
    return KVault()
}