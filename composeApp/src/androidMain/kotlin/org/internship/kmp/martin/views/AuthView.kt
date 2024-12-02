package org.internship.kmp.martin.views

import androidx.compose.foundation.background
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import org.internship.kmp.martin.MainActivity
import org.internship.kmp.martin.core.domain.AppConstants
@Composable
fun AuthView(navController: NavController) {
    val context = LocalContext.current as MainActivity
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(AppConstants.Colors.PRIMARY_DARK_HEX.toColorInt()))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://storage.googleapis.com/pr-newsroom-wp/1/2023/05/Spotify_Primary_Logo_RGB_Green.png"),
                contentDescription = "Spotify Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Spotify Janitor",
                style = MaterialTheme.typography.h6.copy(color = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt())),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {context.initiateSpotifyLogin()},
                modifier = Modifier.padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(AppConstants.Colors.SPOTIFY_GREEN_HEX.toColorInt()))
            ) {
                Text("Login with Spotify", color = Color.White)
            }
        }
    }
}
