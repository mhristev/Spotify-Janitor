package org.internship.kmp.martin.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.internship.kmp.martin.components.SearchTrackItem
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksAction
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoriteTracksView() {
    val viewModel: FavoriteTracksViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Dialog state to show/hide the confirmation modal
    var showDialog by remember { mutableStateOf(false) }
    var trackToDelete by remember { mutableStateOf<Track?>(null) }

    // Function to confirm deletion
    fun confirmDelete(track: Track) {
        trackToDelete = track
        showDialog = true
    }

    // Function to handle delete confirmation
    fun onDeleteConfirmed() {
        trackToDelete?.let {
            viewModel.onAction(FavoriteTracksAction.onTrackDelete(it))
        }
        showDialog = false
    }

    // Function to handle cancel action
    fun onDeleteCancelled() {
        showDialog = false
    }

//    val context = LocalContext.current

//    fun deleteTrack(track: Track) {
//        val trackJson = track.id
//        val inputData = Data.Builder()
//            .putString("track", trackJson)
//            .build()
//
//        val deleteTrackWorkRequest = OneTimeWorkRequestBuilder<TrackDeletionWorker>()
//            .setInputData(inputData)
//            .setInitialDelay(5, java.util.concurrent.TimeUnit.SECONDS)
//            .build()
//
//        WorkManager.getInstance(context).enqueue(deleteTrackWorkRequest)
//    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite Tracks", color = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt())) },
                backgroundColor = Color(AppConstants.Colors.PRIMARY_DARK_HEX.toColorInt()),
                actions = {
                    // Sync Button on TopBar
                    IconButton(
                        onClick = { viewModel.syncronizeTracks() },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,  // Sync Icon
                            contentDescription = "Sync Tracks",
                            tint = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt())
                        )
                    }
                },
                elevation = 4.dp
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(AppConstants.Colors.PRIMARY_DARK_HEX.toColorInt()))
            ) {
                state.lastRemovedTrack?.let { track ->
                    Button(onClick = {
                        viewModel.onAction(FavoriteTracksAction.onUndoDeleteTrack)
                    }) {
                        Text("Undo")
                    }
                }

                LazyColumn() {
                    items(state.tracks, key = { it.id }) { track ->
                        SwipeToDismiss(
                            state = rememberDismissState(
                                confirmStateChange = {
                                    if (it == DismissValue.DismissedToEnd) {
                                        // Show confirmation dialog when swiped to delete
                                        confirmDelete(track)
                                        false // Don't delete immediately
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
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = { viewModel.onAction(FavoriteTracksAction.GetNextFavoriteTracks) },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                border = BorderStroke(1.dp, Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt())),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Load More", color = Color.White)
                            }
                        }
                    }
                }
                if (showDialog) {
                    trackToDelete?.let {
                        DeleteConfirmationDialog(
                            track = it,
                            onConfirm = { onDeleteConfirmed() },
                            onDismiss = { onDeleteCancelled() }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DeleteConfirmationDialog(
    track: Track?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (track != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Confirm Deletion", color = Color.White) },
            text = { Text("Are you sure you want to delete this track?", color = Color.White) },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("Delete", color = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt()))
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt()))
                }
            },
            backgroundColor = Color(AppConstants.Colors.PRIMARY_DARK_HEX.toColorInt())
        )
    }
}
