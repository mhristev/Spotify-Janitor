package org.internship.kmp.martin.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.internship.kmp.martin.items.SearchTrackItem
import org.internship.kmp.martin.presentation.FavoriteTracksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteTracksView() {
    val viewModel: FavoriteTracksViewModel = koinViewModel()
    val tracks by viewModel.cashedTracks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFavoriteTracks()
    }
    Column {
        Button(onClick = { viewModel.syncronizeTracks() }) {
            Text("Syncronize Tracks")
        }
        LazyColumn {
            items(tracks) { track ->
                SearchTrackItem(track)
            }
        }
    }

}