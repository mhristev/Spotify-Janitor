package org.internship.kmp.martin

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.internship.kmp.martin.data.database.spotifyuser.SpotifyUserEntity
import org.internship.kmp.martin.data.domain.SpotifyUser
import org.internship.kmp.martin.presentation.UserViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items

@Composable
fun App() {
    val viewModel: UserViewModel = koinViewModel()
    val context = LocalContext.current
    val users by retrieveAllUsers(viewModel).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { CoroutineScope(Dispatchers.Main).launch {
            addSpotifyUser(viewModel, context)
        } }) {
            Text("Add Spotify User")
        }
        Button(onClick = { retrieveAllUsers(viewModel) }) {
            Text("Retrieve All Users")
        }
        LazyColumn {
            items(users) { user ->
                Text(text = "${user}")
            }
        }
    }
}

private suspend fun addSpotifyUser(viewModel: UserViewModel, context: Context) {
    // Add Spotify user to the database using the viewModel
    val usr = SpotifyUser("1", "2", "3", "4", "5", 6, "7")
    viewModel.addUser(usr)
}

private fun retrieveAllUsers(viewModel: UserViewModel): Flow<List<SpotifyUserEntity>> {
    return viewModel.getCurrentSpotifyUser()
}