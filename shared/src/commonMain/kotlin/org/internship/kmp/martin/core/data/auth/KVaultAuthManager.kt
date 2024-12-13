package org.internship.kmp.martin.core.data.auth

import com.liftric.kvault.KVault
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.core.domain.Result

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

    override fun isTokenExpired(): StateFlow<Boolean> {
        val expiresIn = getExpireTime() ?: return MutableStateFlow(true)
        val currentTimeSeconds = Clock.System.now().epochSeconds
        return MutableStateFlow(currentTimeSeconds > expiresIn)
    }

    override fun hasTokenExpired(): StateFlow<Boolean> {
        val expiresIn = getExpireTime() ?: return MutableStateFlow(true)
        val currentTimeSeconds = Clock.System.now().epochSeconds
        return MutableStateFlow(currentTimeSeconds > expiresIn)
    }


    override fun getAccessToken(): String? {
        return kvault.string(AppConstants.VaultKeys.AUTH_TOKEN)
    }

    private fun getExpireTime(): Long? {
        return kvault.long(AppConstants.VaultKeys.EXPIRE_TIME)
    }

    override fun logoutClear() {
        kvault.deleteObject(AppConstants.VaultKeys.AUTH_TOKEN)
        kvault.deleteObject(AppConstants.VaultKeys.EXPIRE_TIME)
        kvault.deleteObject(AppConstants.VaultKeys.USER_ID)
    }

    override fun getValidAccessToken(): Result<String, DataError.Local> {
        val accessToken = kvault.string(AppConstants.VaultKeys.AUTH_TOKEN) ?: return Result.Error(DataError.Local.NO_ACCESS_TOKEN)
        return if (hasTokenExpired().value) {
            Result.Error(DataError.Local.ACCESS_TOKEN_EXPIRED)
        } else {
            Result.Success(accessToken)
        }
    }

    override fun setAccessToken(accessToken: String) {
        kvault.set(AppConstants.VaultKeys.AUTH_TOKEN, accessToken)
    }

    override fun setExpiresIn(expiresIn: Int) {
        val expireTime = calculateExpireTime(expiresIn)
        kvault.set(AppConstants.VaultKeys.EXPIRE_TIME, expireTime)
    }

    override fun setUserId(userId: String) {
        kvault.set(AppConstants.VaultKeys.USER_ID, userId)
    }

    override fun getUserId(): String? {
        return kvault.string(AppConstants.VaultKeys.USER_ID)
    }
}