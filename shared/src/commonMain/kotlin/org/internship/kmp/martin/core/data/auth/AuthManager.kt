package org.internship.kmp.martin.core.data.auth

import kotlinx.coroutines.flow.StateFlow
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.core.domain.DataError

interface AuthManager {
    fun setAccessToken(accessToken: String)
    fun setExpiresIn(expiresIn: Int)
    fun setUserId(userId: String)
    fun getUserId(): String?
    fun logoutClear()
    fun getValidAccessToken(): Result<String, DataError.Local>
    fun login(accessToken: String, userId: String, expiresIn: Int)
    fun isTokenExpired(): StateFlow<Boolean>
    fun getAccessToken(): String?
    fun hasTokenExpired(): StateFlow<Boolean>
}