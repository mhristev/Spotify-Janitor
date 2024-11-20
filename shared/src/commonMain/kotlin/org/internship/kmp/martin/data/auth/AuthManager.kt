package org.internship.kmp.martin.data.auth

interface AuthManager {
    fun login(accessToken: String)
    fun logout()
    fun getAccessToken(): String?
}