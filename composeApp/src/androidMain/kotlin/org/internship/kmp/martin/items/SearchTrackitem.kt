package org.internship.kmp.martin.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.presentation.FavoriteTracksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchTrackItem(track: Track) {
    val viewModel: FavoriteTracksViewModel = koinViewModel()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = track.name, style = MaterialTheme.typography.h6)
            Text(text = track.artists.joinToString(", ") { it.name }, style = MaterialTheme.typography.body2)
        }
        Button(
            onClick = { viewModel.addTrackToFavorites(track) },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(Icons.Default.Favorite, contentDescription = "Add to Favorites")
        }
    }
}