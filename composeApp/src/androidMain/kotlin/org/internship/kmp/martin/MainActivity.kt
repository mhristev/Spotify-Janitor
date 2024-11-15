package org.internship.kmp.martin
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.TOKEN
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.ERROR
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.CODE
import org.internship.kmp.martin.services.TokenManager
import org.internship.kmp.martin.services.TokenManagerImpl

class MainActivity : ComponentActivity() {
    private val CLIENT_ID = "91be3576121a482e9ad00bb97888f3e8"
    private val REDIRECT_URI = "org.internship.kmp.martin://callback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Button(onClick = { initiateSpotifyLogin() }) {
                Text("Login with Spotify")
            }
        }
    }

    private fun initiateSpotifyLogin() {
        val builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
        builder.setScopes(arrayOf("user-read-email", "user-read-private"))
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
                    // Access token received
                    val accessToken = response.accessToken

                    val time = response.expiresIn // Access token expiration time

                    val refreshToken = response.code // Refresh token
                    // Use the access token as needed

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

//private fun exchangeCodeForToken(code: String) {
//    val client = OkHttpClient()
//    val requestBody = FormBody.Builder()
//        .add("grant_type", "authorization_code")
//        .add("code", code)
//        .add("redirect_uri", REDIRECT_URI)
//        .add("client_id", CLIENT_ID)
//        .add("client_secret", CLIENT_SECRET) // You need to add your client secret here
//        .build()
//
//    val request = Request.Builder()
//        .url("https://accounts.spotify.com/api/token")
//        .post(requestBody)
//        .build()
//
//    client.newCall(request).enqueue(object : Callback {
//        override fun onFailure(call: Call, e: IOException) {
//            // Handle failure
//        }
//
//        override fun onResponse(call: Call, response: Response) {
//            if (response.isSuccessful) {
//                val responseBody = response.body?.string()
//                val json = JSONObject(responseBody)
//                val accessToken = json.getString("access_token")
//                val refreshToken = json.getString("refresh_token")
//                val expiresIn = json.getLong("expires_in")
//
//                // Save the tokens and expiration time as needed
//                tokenManager.saveAccessToken(accessToken, expiresIn)
//                tokenManager.saveRefreshToken(refreshToken)
//            } else {
//                // Handle error
//            }
//        }
//    })
//}
//
//class TokenManager(context: Context) {
//    private val prefs: SharedPreferences = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
//
//    fun saveAccessToken(token: String, expiresIn: Long) {
//        val editor = prefs.edit()
//        editor.putString("access_token", token)
//        editor.putLong("expires_at", System.currentTimeMillis() + expiresIn * 1000)
//        editor.apply()
//    }
//
//    fun saveRefreshToken(token: String) {
//        val editor = prefs.edit()
//        editor.putString("refresh_token", token)
//        editor.apply()
//    }
//
//    fun getAccessToken(): String? {
//        return prefs.getString("access_token", null)
//    }
//
//    fun getRefreshToken(): String? {
//        return prefs.getString("refresh_token", null)
//    }
//
//    fun isTokenExpired(): Boolean {
//        val expiresAt = prefs.getLong("expires_at", 0)
//        return System.currentTimeMillis() > expiresAt
//    }
//
//    fun clearToken() {
//        val editor = prefs.edit()
//        editor.remove("access_token")
//        editor.remove("expires_at")
//        editor.remove("refresh_token")
//        editor.apply()
//    }
//    fun refreshAccessToken(clientId: String, clientSecret: String, callback: (Boolean) -> Unit) {
//        val refreshToken = getRefreshToken() ?: return callback(false)
//        val client = OkHttpClient()
//        val requestBody = FormBody.Builder()
//            .add("grant_type", "refresh_token")
//            .add("refresh_token", refreshToken)
//            .add("client_id", clientId)
//            .add("client_secret", clientSecret)
//            .build()
//
//        val request = Request.Builder()
//            .url("https://accounts.spotify.com/api/token")
//            .post(requestBody)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                callback(false)
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body?.string()
//                    val json = JSONObject(responseBody)
//                    val accessToken = json.getString("access_token")
//                    val expiresIn = json.getLong("expires_in")
//                    saveAccessToken(accessToken, expiresIn)
//                    callback(true)
//                } else {
//                    callback(false)
//                }
//            }
//        })
//    }
//}
