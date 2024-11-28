package org.internship.kmp.martin.core.data.auth

interface AuthManager {
    fun getUserId(): String?
    fun logout()
    fun getAccessToken(): String?
    fun login(accessToken: String, userId: String)
}