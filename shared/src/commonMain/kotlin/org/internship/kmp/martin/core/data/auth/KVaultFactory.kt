package org.internship.kmp.martin.core.data.auth

import com.liftric.kvault.KVault

expect class KVaultFactory {
    fun create(): KVault
}