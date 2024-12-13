package org.internship.kmp.martin

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksAction
import org.internship.kmp.martin.track.presentation.fav_tracks_list.FavoriteTracksViewModel
import org.koin.core.component.KoinComponent

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
                viewModel.onAction(FavoriteTracksAction.OnRemoveTrackByIdGlobally(trackId))
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}