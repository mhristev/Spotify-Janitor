package org.internship.kmp.martin.services

interface TokenManager {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}


expect fun getTokenManager(context: Any? = null): TokenManager
