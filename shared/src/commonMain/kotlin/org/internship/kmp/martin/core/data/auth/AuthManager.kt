package org.internship.kmp.martin.core.data.auth

import kotlinx.coroutines.flow.StateFlow

interface AuthManager {
    fun setAccessToken(accessToken: String)
    fun getUserId(): String?
    fun logoutClear()
    fun getAccessToken(): String?
    fun login(accessToken: String, userId: String, expiresIn: Int)
    fun hasTokenExpired(): StateFlow<Boolean>
}