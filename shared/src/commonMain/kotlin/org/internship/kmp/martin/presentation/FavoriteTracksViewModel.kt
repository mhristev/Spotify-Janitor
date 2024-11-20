package org.internship.kmp.martin.presentation

import androidx.lifecycle.ViewModel
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.data.repository.TrackRepository


class FavoriteTracksViewModel(private val trackRepository: TrackRepository): ViewModel() {

    @NativeCoroutinesState
    val favoriteTracks: Flow<List<Track>> =
        trackRepository.getFavoriteTracks()

    private var lastRemovedTrack: Track? = null

    fun removeTrack(track: Track) {
        trackRepository.removeFavoriteTrack(track)
        lastRemovedTrack = track
    }

    fun undoRemoveTrack() {
        lastRemovedTrack?.let {
            trackRepository.addFavoriteTrack(it)
            lastRemovedTrack = null
        }
    }

}