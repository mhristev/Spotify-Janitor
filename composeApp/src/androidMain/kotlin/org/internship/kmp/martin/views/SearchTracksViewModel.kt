package org.internship.kmp.martin.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.internship.kmp.martin.components.SearchTrackItem
import org.internship.kmp.martin.track.presentation.BrowseTracksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchTracksView() {
    val viewModel: BrowseTracksViewModel = koinViewModel()
    val tracks by viewModel.cashedTracks.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { viewModel.searchTracks(searchQuery) }) {
                Text("Search")
            }
        }
        LazyColumn {
            items(tracks) { track ->
                SearchTrackItem(track)
            }
        }
    }
}