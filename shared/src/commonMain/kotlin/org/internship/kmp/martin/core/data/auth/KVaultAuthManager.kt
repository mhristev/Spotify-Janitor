package org.internship.kmp.martin.core.data.auth

import com.liftric.kvault.KVault
import kotlinx.datetime.Clock
import org.internship.kmp.martin.core.domain.AppConstants

class KVaultAuthManager(private val kvault: KVault): AuthManager {

    override fun login(accessToken: String, userId: String, expiresIn: Int) {
        kvault.set(AppConstants.VaultKeys.AUTH_TOKEN, accessToken)
        kvault.set(AppConstants.VaultKeys.USER_ID, userId)

        val expireTime = calculateExpireTime(expiresIn)
        kvault.set(AppConstants.VaultKeys.EXPIRE_TIME, expireTime.toString())
    }

    private fun calculateExpireTime(expiresIn: Int): Long {
        val currentTimeMillis = Clock.System.now().epochSeconds
        return currentTimeMillis + expiresIn
    }

    override fun hasTokenExpired(): Boolean {
        val expiresIn = getExpireTime() ?: return true
        val currentTimeSeconds = Clock.System.now().epochSeconds
        return currentTimeSeconds > expiresIn
    }

    private fun getExpireTime(): Long? {
        return kvault.string(AppConstants.VaultKeys.EXPIRE_TIME)?.toLong()
    }

    override fun logoutClear() {
        kvault.deleteObject(AppConstants.VaultKeys.AUTH_TOKEN)
        kvault.deleteObject(AppConstants.VaultKeys.EXPIRE_TIME)
        kvault.deleteObject(AppConstants.VaultKeys.USER_ID)
    }

    override fun getAccessToken(): String? {
        return kvault.string(AppConstants.VaultKeys.AUTH_TOKEN)
    }

    override fun setAccessToken(accessToken: String) {
        kvault.set(AppConstants.VaultKeys.AUTH_TOKEN, accessToken)
    }

    override fun getUserId(): String? {
        return kvault.string(AppConstants.VaultKeys.USER_ID)
    }
}