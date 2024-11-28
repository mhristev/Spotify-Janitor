package org.internship.kmp.martin.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import org.internship.kmp.martin.components.UserInfoRow
import org.internship.kmp.martin.spotify_user.presentation.UserProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserProfileView() {
    val viewModel: UserProfileViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spotify Profile", color = Color.White) },
                backgroundColor = Color.Black,
                actions = {
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White)
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFF1F1F1F)) // Dark background color
            ) {
                // Profile Header with Background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color(0xFF50009C)) // Purple background
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(state.user?.imageUrl),
                        contentDescription = "User Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = state.user?.displayName ?: "",
                        style = TextStyle(color = Color.White, fontSize = 24.sp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    )
                }

                // Account Info Sections
                Spacer(modifier = Modifier.height(16.dp))
                state.user?.email?.let { UserInfoRow(label = "EMAIL", value = it, icon = Icons.Default.Person) }
                Divider(color = Color.Gray, thickness = 1.dp)

                Spacer(modifier = Modifier.height(16.dp))
                state.user?.country?.let { UserInfoRow(label = "COUNTRY", value = it, icon = Icons.Default.Person) }
                Divider(color = Color.Gray, thickness = 1.dp)

                Spacer(modifier = Modifier.height(16.dp))
                UserInfoRow(label = "FOLLOWERS", value = state.user?.followers.toString(), icon = Icons.Default.Person)
                Divider(color = Color.Gray, thickness = 1.dp)

                Spacer(modifier = Modifier.height(16.dp))
                state.user?.product?.let { UserInfoRow(label = "PRODUCT", value = it, icon = Icons.Default.Person) }
                Divider(color = Color.Gray, thickness = 1.dp)
            }
        }
    )

}