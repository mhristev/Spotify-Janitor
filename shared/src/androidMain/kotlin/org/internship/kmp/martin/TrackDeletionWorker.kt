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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TrackDeletionWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {
    private val trackRepository: TrackRepository by inject()

    override suspend fun doWork(): Result {
        val trackJson = inputData.getString("track")
        if (trackJson.isNullOrEmpty()) {
            return Result.failure()
        }

        val track = parseTrackJson(trackJson)
        return try {
            withContext(Dispatchers.IO) {
                trackRepository.removeFavoriteTrack(track)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun parseTrackJson(json: String): Track {
        return Json.decodeFromString(json)
    }
}