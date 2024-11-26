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
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.internship.kmp.martin.components.SearchTrackItem
import org.internship.kmp.martin.presentation.fav_tracks_list.FavoriteTracksAction
import org.internship.kmp.martin.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoriteTracksView() {
    val viewModel: FavoriteTracksViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


    Column {
        Button(onClick = { viewModel.syncronizeTracks() }) {
            Text("Synchronize Tracks")
        }
        state.lastRemovedTrack?.let { track ->
            Button(onClick = {
                viewModel.onAction(FavoriteTracksAction.onUndoDeleteTrack)
            }) {
                Text("Undo")
            }
        }

        LazyColumn {
            items(state.tracks, key = { it.id }) { track ->
                SwipeToDismiss(
                    state = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToEnd) {
                                viewModel.onAction(FavoriteTracksAction.onTrackDelete(track))
                                true
                            } else {
                                false
                            }
                        }
                    ),
                    background = { },
                    dismissContent = {
                        SearchTrackItem(track)
                    }
                )
            }
        }
    }
}