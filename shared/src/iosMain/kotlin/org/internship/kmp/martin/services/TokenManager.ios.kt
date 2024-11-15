package org.internship.kmp.martin.services
import com.liftric.kvault.KVault
import org.internship.kmp.martin.services.TokenManager

class TokenManagerImpl : TokenManager {
    private val vault = KVault()

    override fun saveToken(token: String) {
        vault.set("auth_token", token)
    }

    override fun getToken(): String? {
        return vault.string("auth_token")
    }

    override fun clearToken() {
        vault.deleteObject("auth_token")
    }
}

actual fun getTokenManager(context: Any?): TokenManager {
    return TokenManagerImpl()
}