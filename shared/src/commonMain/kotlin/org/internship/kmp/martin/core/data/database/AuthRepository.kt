package org.internship.kmp.martin.core.data.database

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.StateFlow
import org.internship.kmp.martin.core.domain.DataError


interface AuthRepository {
    @NativeCoroutines
    fun isUserLoggedIn(): StateFlow<Boolean>
    suspend fun login(accessToken: String, expiresIn: Int): DataError?
    fun logout()
    fun isUserAuthenticated(): Boolean
}