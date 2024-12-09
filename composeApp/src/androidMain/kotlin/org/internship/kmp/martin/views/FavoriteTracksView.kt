package org.internship.kmp.martin.views

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
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
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import org.internship.kmp.martin.TrackDeletionWorker
import org.internship.kmp.martin.components.RemoveConfirmationDialog
import org.internship.kmp.martin.components.TrackItem
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksAction
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.UUID
import java.util.concurrent.TimeUnit


//state.lastRemovedTrack?.let { track ->
//    if (viewModel.showUndoButton) {
//        Button(onClick = {
//            viewModel.onAction(FavoriteTracksAction.onUndoDeleteTrack)
//            removeTrackIdFromUserDefaults()
//            viewModel.showUndoButton = false
//        }) {
//            Text("Undo")
//        }
//    }
//}
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoriteTracksView() {
    val context = LocalContext.current
    val viewModel: FavoriteTracksViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showUndoButton by mutableStateOf(false)
    var workRequestId by mutableStateOf(UUID.randomUUID())


    var showDialog by remember { mutableStateOf(false) }
    var trackToDelete by remember { mutableStateOf<Track?>(null) }

    fun cancelScheduledTrackDeletion() {
        WorkManager.getInstance(context).cancelWorkById(workRequestId)
    }

    fun confirmDelete(track: Track) {
        trackToDelete = track
        showDialog = true
    }
    fun scheduleTrackDeletion(trackId: String) {
        val inputData = Data.Builder()
            .putString("trackId", trackId)
            .build()

        val request = OneTimeWorkRequestBuilder<TrackDeletionWorker>()
            .setInitialDelay(3, TimeUnit.SECONDS)
            .setInputData(inputData)
            .build()

        workRequestId = request.id

        WorkManager.getInstance(context).enqueue(request)
    }

    fun showUndoButton() {
        showUndoButton = true
        Handler(Looper.getMainLooper()).postDelayed({
            if (showUndoButton) {
                showUndoButton = false
            }
        }, 3000)
    }

    fun onDeleteConfirmed() {
        trackToDelete?.let {
            viewModel.removeTrackFromCashedList(it)
            scheduleTrackDeletion(it.id)
        }
        showDialog = false
        showUndoButton()
    }

    fun onDeleteCancelled() {
        showDialog = false
        showUndoButton = false
        viewModel.restoreLastRemovedTrackToCashedList()
        cancelScheduledTrackDeletion()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite Tracks", color = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt())) },
                backgroundColor = Color(AppConstants.Colors.PRIMARY_DARK_HEX.toColorInt()),
                actions = {
                    IconButton(
                        onClick = { viewModel.onAction(FavoriteTracksAction.SyncronizeTracks) },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
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
                if (showUndoButton) {
                    Button(onClick = {
                        onDeleteCancelled()
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
                                        confirmDelete(track)
                                        false
                                    } else {
                                        false
                                    }
                                }
                            ),
                            background = { },
                            dismissContent = {
                                TrackItem(track, onAddToFavoritesClick = null)
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
                        RemoveConfirmationDialog(
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


