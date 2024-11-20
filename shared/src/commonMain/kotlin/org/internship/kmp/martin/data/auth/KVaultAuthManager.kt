package org.internship.kmp.martin.data.auth

import com.liftric.kvault.KVault

class KVaultAuthManager(private val kVault: KVault): AuthManager {

    override fun login(accessToken: String) {
        kVault.set("auth_token", accessToken)
    }

    override fun logout() {
        kVault.deleteObject("auth_token")
    }

    override fun getAccessToken(): String? {
        return kVault.string("auth_token")
    }
}