package org.internship.kmp.martin.core.data.database

import org.internship.kmp.martin.core.domain.DataError


interface AuthRepository {
    fun isUserLoggedIn(): Boolean
    suspend fun login(accessToken: String, expiresIn: Int): DataError?
    fun logout()
}