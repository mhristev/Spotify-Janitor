package org.internship.kmp.martin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.data.repository.TrackRepository


class FavoriteTracksViewModel(private val trackRepository: TrackRepository): ViewModel() {

    @NativeCoroutinesState
    var cashedTracks: MutableStateFlow<List<Track>> = MutableStateFlow(emptyList())

    private val _state = MutableStateFlow(TrackListState())

    fun fetchFavoriteTracks() {
        viewModelScope.launch {
            val result = trackRepository.getFavoriteTracks()
            if (result is Result.Success) {
                cashedTracks.value = result.data
            }
        }
    }

    fun addTrackToFavorites(track: Track) {
        println("Hello")
//        trackRepository.addFavoriteTrack(track)
    }


    fun syncronizeTracks() {
        viewModelScope.launch {
            when (val result = trackRepository.synchronizeTracks()) {
                is Result.Success -> {
                    // Collect the Flow of tracks and update cashedTracks
                    result.data.collect { tracks ->
                        cashedTracks.value = tracks.sortedByDescending { it.addedAt }
                    }
                }
                is Result.Error -> {
                    // Handle the error appropriately
                    // For example, update _state with an error state
//                    _state.value = TrackListState(error = result.error)
                }
            }
        }
    }

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