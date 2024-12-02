package org.internship.kmp.martin.core.data.auth

interface AuthManager {
    fun setAccessToken(accessToken: String)
    fun getUserId(): String?
    fun logoutClear()
    fun getAccessToken(): String?
    fun login(accessToken: String, userId: String, expiresIn: Int)
    fun hasTokenExpired(): Boolean
}