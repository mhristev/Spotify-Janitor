package org.internship.kmp.martin.services
import android.content.Context
import com.liftric.kvault.KVault
import org.internship.kmp.martin.services.TokenManager

class TokenManagerImpl(context: Context) : TokenManager {
    private val vault = KVault(context)

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
    require(context is Context) { "Context must be provided for Android" }
    return TokenManagerImpl(context)
}