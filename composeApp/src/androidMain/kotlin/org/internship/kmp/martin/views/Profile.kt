//package org.internship.kmp.martin.views
//
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import coil3.compose.rememberAsyncImagePainter
//
//
//@Composable
//fun SpotifyUserProfileScreen(
//    username: String,
//    email: String,
//    country: String,
//    followers: Int,
//    product: String,
//    imageUrl: String,
//    onLogoutClick: () -> Unit
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Spotify Profile", color = Color.White) },
//                backgroundColor = Color.Black,
//                actions = {
//                    IconButton(onClick = onLogoutClick) {
//                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White)
//                    }
//                }
//            )
//        },
//        content = { padding ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding)
//                    .background(Color(0xFF1F1F1F)) // Dark background color
//            ) {
//                // Profile Header with Background
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(250.dp)
//                        .background(Color(0xFF50009C)) // Purple background
//                ) {
//                    Image(
//                        painter = rememberAsyncImagePainter(imageUrl),
//                        contentDescription = "User Image",
//                        modifier = Modifier
//                            .size(120.dp)
//                            .clip(CircleShape)
//                            .align(Alignment.Center),
//                        contentScale = ContentScale.Crop
//                    )
//                    Text(
//                        text = username,
//                        style = TextStyle(color = Color.White, fontSize = 24.sp),
//                        modifier = Modifier
//                            .align(Alignment.BottomCenter)
//                            .padding(bottom = 16.dp)
//                    )
//                }
//
//                // Account Info Sections
//                Spacer(modifier = Modifier.height(16.dp))
//                UserInfoRow(label = "Full Name", value = "as", icon = Icons.Default.Person)
//                Divider(color = Color.Gray, thickness = 1.dp)
//
//                Spacer(modifier = Modifier.height(16.dp))
//                UserInfoRow(label = "Full Name", value = "asd", icon = Icons.Default.Person)
//                Divider(color = Color.Gray, thickness = 1.dp)
//
//                Spacer(modifier = Modifier.height(16.dp))
//                UserInfoRow(label = "Full Name", value = "asd", icon = Icons.Default.Person)
//                Divider(color = Color.Gray, thickness = 1.dp)
//
//                Spacer(modifier = Modifier.height(16.dp))
//                UserInfoRow(label = "Full Name", value = "as", icon = Icons.Default.Person)
//                Divider(color = Color.Gray, thickness = 1.dp)
//            }
//        }
//    )
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewSpotifyUserProfileScreen() {
//    SpotifyUserProfileScreen(
//        username = "SpotifyUser",
//        email = "user@spotify.com",
//        country = "USA",
//        followers = 2500,
//        product = "Premium",
//        imageUrl = "https://www.example.com/spotify_user.jpg",
//        onLogoutClick = {}
//    )
//}