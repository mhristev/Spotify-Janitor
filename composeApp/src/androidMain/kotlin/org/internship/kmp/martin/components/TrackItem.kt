package org.internship.kmp.martin.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil3.compose.rememberAsyncImagePainter
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackItem(track: Track, onAddToFavoritesClick: ((track:Track) -> Unit)? = null) {
    var imageLoadResult by remember { mutableStateOf<Result<Painter>?>(null) }
    val painter = rememberAsyncImagePainter(
        model = track.album.imageUrl,
        onSuccess = {
            imageLoadResult = if (it.painter.intrinsicSize.width > 1 && it.painter.intrinsicSize.height > 1) {
                Result.success(it.painter)
            } else {
                Result.failure(Exception("Invalid image size"))
            }
        },
        onError = {
            it.result.throwable.printStackTrace()
            imageLoadResult = Result.failure(it.result.throwable)
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val result = imageLoadResult) {
                null -> CircularProgressIndicator(color = Color(AppConstants.Colors.PRIMARY_PURPLE_HEX.toColorInt()))
                else -> {
                    Image(
                        painter = if (result.isSuccess) painter else painterResource(id = android.R.drawable.ic_menu_report_image),
                        contentDescription = "Track Image",
                        contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = track.name, style = MaterialTheme.typography.body1, color = Color(
                AppConstants.Colors.PRIMARY_TEXT_WHiTE_HEX.toColorInt())
            )
            Text(text = track.artists.joinToString(" · ") { it.name }, style = MaterialTheme.typography.body2, color = Color(
                AppConstants.Colors.SECONDARY_TEXT_GREY_HEX.toColorInt())
            )
        }
        if (onAddToFavoritesClick != null) {
            Button(
                onClick = { onAddToFavoritesClick(track) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Add to Favorites")
            }
        }

    }
}