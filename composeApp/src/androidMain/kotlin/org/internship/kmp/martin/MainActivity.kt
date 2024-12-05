package org.internship.kmp.martin
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.TOKEN
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.ERROR
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.internship.kmp.martin.core.data.database.AuthRepository
import org.internship.kmp.martin.core.presentation.AuthViewModel
import org.internship.kmp.martin.core.presentation.LoginViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.koinInject


class MainActivity : ComponentActivity() {
    private val CLIENT_ID = "91be3576121a482e9ad00bb97888f3e8"
    private val REDIRECT_URI = "org.internship.kmp.martin://callback"

    private var isAuthenticated = MutableStateFlow(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppNavigator(navController = rememberNavController())
        }
    }
    fun initiateSpotifyLogin() {
        val builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
        builder.setScopes(arrayOf("user-read-email", "user-read-private", "user-library-read", "user-library-modify"))
        val request = builder.build()
        AuthorizationClient.openLoginInBrowser(this, request)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri = intent.data
        if (uri != null) {
            val response = AuthorizationResponse.fromUri(uri)
            when (response.type) {
                TOKEN -> {
                    val accessToken = response.accessToken
                    val expiresIn = response.expiresIn
                    isAuthenticated.update { true }
                    val authRepository: AuthRepository = get()
                    lifecycleScope.launch {
                        authRepository.login(accessToken, expiresIn)
                    }
//                    val viewModel: AuthViewModel by viewModel()
//
//                    viewModel.login(accessToken, expiresIn)
                }
                ERROR -> {
                    // TODO()
                }
                else -> {
                    // TODO()
                }
            }
        }
    }
}