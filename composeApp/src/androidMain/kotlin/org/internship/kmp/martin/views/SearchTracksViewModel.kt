package org.internship.kmp.martin.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.internship.kmp.martin.components.SearchTrackItem
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.track.presentation.BrowseTracksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchTracksView() {
    val viewModel: BrowseTracksViewModel = koinViewModel()
    val tracks by viewModel.cashedTracks.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    var lastQuery by remember { mutableStateOf("") }
    val debouncePeriod = 500L // 1 second debounce period
    val coroutineScope = rememberCoroutineScope()

    val darkBackgroundColor = Color(AppConstants.Colors.PRIMARY_DARK_HEX.toColorInt())

    val whiteTextColor = Color(AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackgroundColor) // Use dark background here
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Search", color = whiteTextColor) },
                    backgroundColor = darkBackgroundColor // Use purple for the top bar
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .background(darkBackgroundColor) // Apply dark background here as well
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
                                coroutineScope.launch {
                                    delay(debouncePeriod)
                                    if (searchQuery == newQuery && searchQuery != lastQuery) {
                                        lastQuery = searchQuery
                                        viewModel.searchTracks(searchQuery)
                                    }
                                }
                            },
                            label = { Text("What do you want to listen to?", color = Color.Gray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(whiteTextColor, shape = RoundedCornerShape(8.dp))
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(tracks) { track ->
                            SearchTrackItem(track)
                        }
                    }
                }
            }
        )
    }
}