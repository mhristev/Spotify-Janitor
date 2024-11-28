package org.internship.kmp.martin.core.data.auth

import com.liftric.kvault.KVault

class KVaultAuthManager(private val kvault: KVault): AuthManager {

    override fun login(accessToken: String, userId: String) {
        kvault.set("auth_token", accessToken)
        kvault.set("user_id", userId)
    }

    override fun logout() {
        TODO()
//        kVault.deleteObject("auth_token")
    }

    override fun getAccessToken(): String? {
        return kvault.string("auth_token")
    }

    override fun getUserId(): String? {
        return kvault.string("user_id")
    }
}