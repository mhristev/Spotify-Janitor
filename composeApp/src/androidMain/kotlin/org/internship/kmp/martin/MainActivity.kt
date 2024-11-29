package org.internship.kmp.martin
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.work.Configuration
import androidx.work.WorkManager
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.TOKEN
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.ERROR
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.CODE
import org.internship.kmp.martin.core.presentation.LaunchScreenViewModel
import org.internship.kmp.martin.views.LoginView
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    private val CLIENT_ID = "91be3576121a482e9ad00bb97888f3e8"
    private val REDIRECT_URI = "org.internship.kmp.martin://callback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build()
        )
        setContent {
//            applicationContext.deleteDatabase(RoomAppDatabase.DB_NAME_FAV_TRACK)
//            applicationContext.deleteDatabase(RoomAppDatabase.DB_NAME_SPOTIFY_USER)
            LoginView({ initiateSpotifyLogin() })
        }
    }
    fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
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
                    val expires_int = response.expiresIn
                    setContent{
                        val viewModel: LaunchScreenViewModel = koinViewModel()
                        LaunchedEffect(Unit) {
                            viewModel.login(accessToken)
                        }
                        App()
                    }
                    // Refresh token how to!!!
                   // https://github.com/spotify/android-sdk/issues/255#issuecomment-1825798274
                }
                ERROR -> {
                    // Handle error
                }
                CODE -> {
                    // Authorization code received
                    val code = response.code
                    //exchangeCodeForToken(code)
                    // Exchange code for access token if necessary
                }
                else -> {
                    // Handle other cases
                }
            }
        }
    }
}