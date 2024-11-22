package org.internship.kmp.martin.data.auth

import com.liftric.kvault.KVault

class KVaultAuthManager(private val kvault: KVault): AuthManager {

    override fun login(accessToken: String) {
        kvault.set("auth_token", accessToken)
    }

    override fun logout() {
        TODO()
//        kVault.deleteObject("auth_token")
    }

    override fun getAccessToken(): String? {
        TODO()
//        return kVault.string("auth_token")
    }
}