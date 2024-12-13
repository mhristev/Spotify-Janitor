package org.internship.kmp.martin.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.launch
import org.internship.kmp.martin.components.TrackItem
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.presentation.browse_tracks.BrowseTracksAction
import org.internship.kmp.martin.track.presentation.browse_tracks.BrowseTracksViewModel
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun BrowseTracksView() {
    val viewModel: BrowseTracksViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var lastQuery by remember { mutableStateOf("") }

    val debouncePeriod = 500L

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val darkBackgroundColor = Color(AppConstants.Colors.PRIMARY_DARK_HEX.toColorInt())
    val whiteTextColor = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt())

    fun performSearch(newQuery: String) {
        coroutineScope.launch {
            kotlinx.coroutines.delay(debouncePeriod)
            if (searchQuery == newQuery && searchQuery != lastQuery) {
                lastQuery = searchQuery
                viewModel.onAction(BrowseTracksAction.onSearch(searchQuery))
            }
        }
    }

    fun showErrorMessage() {
        coroutineScope.launch {
            state.errorString?.let {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.onAction(BrowseTracksAction.OnErrorMessageShown)
        }
    }

    fun performAddToFavorites(track: Track) {
        viewModel.onAction(BrowseTracksAction.onTrackAddToFavorites(track))
    }


    fun showBla() {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "added to favorites!",
                duration = SnackbarDuration.Short
            )
        }
        viewModel.onAction(BrowseTracksAction.OnSaveSuccessShown)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Search", color = whiteTextColor) },
                backgroundColor = darkBackgroundColor
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(darkBackgroundColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { newQuery ->
                            searchQuery = newQuery
                            performSearch(newQuery)
                        },
                        label = { Text("What do you want to listen to?", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(whiteTextColor, shape = RoundedCornerShape(8.dp))
                    )
                }
                if (state.isLoadingTracks) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(darkBackgroundColor),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = whiteTextColor)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.tracks) { track ->
                            TrackItem(track, onAddToFavoritesClick = {
                                performAddToFavorites(track)

                            })
                        }
                    }
                }
                if (state.isSavingToFavoritesSuccess) {
                    showBla()
                }
                if (state.errorString != null) {
                    showErrorMessage()
                }
            }
        }
    )
}
