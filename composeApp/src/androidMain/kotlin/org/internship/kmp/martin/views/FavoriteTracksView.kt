package org.internship.kmp.martin.views

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import org.internship.kmp.martin.TrackDeletionWorker
import org.internship.kmp.martin.components.RemoveConfirmationDialog
import org.internship.kmp.martin.components.TrackItem
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksAction
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.internship.kmp.martin.track.presentation.fav_tracks_list.UIEvent
import org.koin.androidx.compose.koinViewModel
import java.util.UUID
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavoriteTracksView() {
    val context = LocalContext.current
    val viewModel: FavoriteTracksViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    var workRequestId by remember {mutableStateOf(UUID.randomUUID())}
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var trackToDelete by remember { mutableStateOf<Track?>(null) }

    val uiEvents = viewModel.uiEvents

    fun cancelScheduledTrackDeletion() {
        WorkManager.getInstance(context).cancelWorkById(workRequestId)
    }

    fun confirmDelete(track: Track) {
        trackToDelete = track
        viewModel.onAction(FavoriteTracksAction.OnShowDeletionDialog)
    }

    fun scheduleTrackDeletion(trackId: String) {
        val inputData = Data.Builder()
            .putString("trackId", trackId)
            .build()

        val request = OneTimeWorkRequestBuilder<TrackDeletionWorker>()
            .setInitialDelay(4, TimeUnit.SECONDS)
            .setInputData(inputData)
            .build()

        workRequestId = request.id

        WorkManager.getInstance(context).enqueue(request)
    }

    fun showUndoSnackbar() {
        coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = "Track removed ",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                cancelScheduledTrackDeletion()
                viewModel.onAction(FavoriteTracksAction.OnRestoreLastRemovedTrackLocally)
            }
        }
    }


    fun showUndoButton() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (state.isShowingUndoButton) {
                viewModel.onAction(FavoriteTracksAction.OnHideUndoOption)
            }
        }, 3000)
    }

     fun onDeleteConfirmed() {
        trackToDelete?.let {
            viewModel.onAction(FavoriteTracksAction.OnRemoveTrackLocally(it))
            scheduleTrackDeletion(it.id)
            showUndoSnackbar()
        }
         viewModel.onAction(FavoriteTracksAction.OnHideDeletionDialog)
        showUndoButton()
    }

     fun onDeleteDialogCancelled() {
        viewModel.onAction(FavoriteTracksAction.OnHideDeletionDialog)
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite Tracks", color = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt())) },
                backgroundColor = Color(AppConstants.Colors.PRIMARY_DARK_HEX.toColorInt()),
                actions = {
                    if (state.isLoadingSync) {
                        CircularProgressIndicator(
                            color = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt()),
                            modifier = Modifier.size(30.dp).padding(5.dp)
                        )
                    } else {
                        IconButton(
                            onClick = {
                                viewModel.onAction(FavoriteTracksAction.OnSyncTracks)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Sync Tracks",
                                tint = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt())
                            )
                        }
                    }
                },
                elevation = 4.dp
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(AppConstants.Colors.PRIMARY_DARK_HEX.toColorInt()))
            ) {
                LazyColumn() {
                    items(state.cashedTracks, key = { it.id }) { track ->
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
                                onClick = {
                                    viewModel.onAction(FavoriteTracksAction.OnGetNextFavoriteTracks) },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                border = BorderStroke(1.dp, Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt())),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Load More", color = Color.White)
                            }
                        }
                    }
                }
                if (state.isShowingDeleteConfirmation) {
                    trackToDelete?.let {
                        RemoveConfirmationDialog(
                            track = it,
                            onConfirm = {
                                    onDeleteConfirmed()
                                        },
                            onDismiss = {
                                onDeleteDialogCancelled()
                            }
                        )
                    }
                }
                LaunchedEffect(Unit) {
                    uiEvents.collect { event ->
                        when (event) {
                            is UIEvent.ShowError -> {
                                snackbarHostState.showSnackbar(
                                    message = event.message,
                                    duration = SnackbarDuration.Short
                                )
                            }
                            is UIEvent.ShowSuccess -> {
                                snackbarHostState.showSnackbar(
                                    message = event.message,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}