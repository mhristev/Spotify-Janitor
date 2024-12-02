package org.internship.kmp.martin
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.TOKEN
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.ERROR
import org.internship.kmp.martin.core.presentation.LoginViewModel
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    private val CLIENT_ID = "91be3576121a482e9ad00bb97888f3e8"
    private val REDIRECT_URI = "org.internship.kmp.martin://callback"

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
                    setContent {
                        val viewModel: LoginViewModel = koinViewModel()
                        viewModel.login(accessToken, expiresIn)
                        App(navController = rememberNavController())
                    }
                }
                ERROR -> {
                    // Handle error
                }
                else -> {
                    // Handle other cases
                }
            }
        }
    }
}