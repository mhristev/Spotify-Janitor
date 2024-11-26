package org.internship.kmp.martin.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.internship.kmp.martin.components.SearchTrackItem
import org.internship.kmp.martin.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoriteTracksView() {
    val viewModel: FavoriteTracksViewModel = koinViewModel()
    val tracks by viewModel.cashedTracks.collectAsState(emptyList())
    var localTracks by remember { mutableStateOf(tracks) }
    var recentlyDeletedTrack by remember { viewModel.lastRemovedTrack}

    LaunchedEffect(tracks) {
        localTracks = tracks
    }

    Column {
        Button(onClick = { viewModel.syncronizeTracks() }) {
            Text("Synchronize Tracks")
        }
        recentlyDeletedTrack?.let { track ->
            Button(onClick = {
                localTracks = listOf(track) + localTracks
                viewModel.undoRemoveTrack()
                recentlyDeletedTrack = null
            }) {
                Text("Undo")
            }
        }

        LazyColumn {
            items(localTracks, key = {it.id}) { track ->
                SwipeToDismiss(
                    state = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToEnd) {
                                localTracks = localTracks.filterNot { it.id == track.id }
                                viewModel.removeTrack(track)
                                true
                            } else {
                                false
                            }
                        }
                    ),
                    background = {  },
                    dismissContent = {
                        SearchTrackItem(track)
                    }
                )
            }
        }

    }
}