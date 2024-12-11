package org.internship.kmp.martin

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.internship.kmp.martin.track.data.repository.TrackRepository
import org.internship.kmp.martin.track.domain.Track
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksAction
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TrackDeletionWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {
    val viewModel: FavoriteTracksViewModel = getKoin().get<FavoriteTracksViewModel>()

    override suspend fun doWork(): Result {
        val trackId = inputData.getString("trackId")
        if (trackId.isNullOrEmpty()) {
            return Result.failure()
        }
        return try {
            withContext(Dispatchers.IO) {
                viewModel.onAction(FavoriteTracksAction.onRemoveTrackById(trackId))
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}