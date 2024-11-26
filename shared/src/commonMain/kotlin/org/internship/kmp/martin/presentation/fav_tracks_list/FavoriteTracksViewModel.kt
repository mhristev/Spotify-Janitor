package org.internship.kmp.martin.presentation.fav_tracks_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.data.repository.TrackRepository


class FavoriteTracksViewModel(private val trackRepository: TrackRepository): ViewModel() {

//    @NativeCoroutinesState
//    var cashedTracks: MutableStateFlow<List<Track>> = MutableStateFlow(emptyList())

    private val _state = MutableStateFlow(FavoriteTracksState())

    @NativeCoroutinesState
    val state = _state
        .onStart {
            observeFavoriteTracks()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )



    fun onAction(action: FavoriteTracksAction) {

        when (action) {
            is FavoriteTracksAction.onTrackDelete -> {

                _state.update { currentState ->
                    val updatedTracks = currentState.tracks.filter { it.id != action.track.id }
                    currentState.copy(tracks = updatedTracks, lastRemovedTrack = action.track)
                }
                removeTrack(action.track)
            }
            is FavoriteTracksAction.onUndoDeleteTrack -> {
                val track = _state.value.lastRemovedTrack ?: return

                _state.update { currentState ->
                    currentState.copy(tracks = listOf(track) + currentState.tracks, lastRemovedTrack = null)
                }
                undoRemoveTrack(track)
            }
            is FavoriteTracksAction.SyncronizeTracks -> syncronizeTracks()
        }
    }

    fun observeFavoriteTracks() {
        viewModelScope.launch {
            val result = trackRepository.getFavoriteTracks()
            if (result is Result.Success) {
                _state.update { it.copy(tracks = result.data) }
            }
        }
    }
    private fun removeTrack(track: Track) {
        viewModelScope.launch {
            trackRepository.removeFavoriteTrack(track)
        }
    }

    fun addTrackToFavorites(track: Track) {
        viewModelScope.launch { trackRepository.addFavoriteTrack(track) }
    }

    private fun undoRemoveTrack(track: Track) {
        viewModelScope.launch {
            trackRepository.addFavoriteTrack(track)
        }
    }

    fun syncronizeTracks() {
        viewModelScope.launch {
            when (val result = trackRepository.synchronizeTracks()) {
                is Result.Success -> {
                    result.data.collect { tracks ->
                        _state.update { it.copy(tracks = tracks.sortedByDescending { it.addedAt }) }
                    }
                }
                is Result.Error -> {
                    // Handle the error appropriately
                }
            }
        }
    }
}