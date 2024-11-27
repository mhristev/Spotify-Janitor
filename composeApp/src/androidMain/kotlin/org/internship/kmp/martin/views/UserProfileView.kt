package org.internship.kmp.martin.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.internship.kmp.martin.spotify_user.presentation.UserViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserProfileView() {
    val viewModel: UserViewModel = koinViewModel()
    val users by viewModel.getCurrentSpotifyUser().collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "User Profile",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = { viewModel.getCurrentSpotifyUser() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Retrieve All Users")
        }
        LazyColumn {
            items(users) { user ->
                Text(
                    text = user.toString(),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}