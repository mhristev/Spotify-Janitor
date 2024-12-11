package org.internship.kmp.martin.track.presentation.fav_tracks_list

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.data.repository.TrackRepository


class FavoriteTracksViewModel(private val trackRepository: TrackRepository): ViewModel() {
    private val _state = MutableStateFlow(FavoriteTracksState())

    private val limitTracksPerPage = AppConstants.Limits.MIN_TRACKS_TO_DISPLAY

    @NativeCoroutinesState
    val state = _state
        .onStart {
            loadFavoriteTracks()
            syncronizeTracks()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )
    private fun loadFavoriteTracks() {
        viewModelScope.launch {
            trackRepository.getFavoriteTracks()
                .onSuccess { tracksFlow ->
                    tracksFlow.collect { tracks ->
                        _state.update {
                            it.copy(tracks = flowOf(tracks.sortedByDescending { track ->track.addedAt }))
                        }

                    }

                }
        }
    }

    private fun removeTrackLocally(track: Track) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    lastRemovedTrack = track
                )
            }
            trackRepository.removeFavoriteTrackLocally(track)
        }
    }

    private fun restoreLastRemovedTrackLocally() {
        val track = _state.value.lastRemovedTrack ?: return
        viewModelScope.launch {
            _state.update {
                trackRepository.restoreTrackToDao(track)
                it.copy(lastRemovedTrack = null)
            }
        }
    }

    private fun removeTrackGlobally(id: String) {
        viewModelScope.launch {
            val track = _state.value.tracks.firstOrNull()?.find { it.id == id } ?: _state.value.lastRemovedTrack ?: return@launch
            trackRepository.removeFavoriteTrackGlobally(track)
            _state.update {
                it.copy(lastRemovedTrack = null)
            }
        }
    }

    private fun loadNextFavoriteTracks() {
        viewModelScope.launch {
            val currentTracksCount = _state.value.tracks.firstOrNull()?.size ?: 0
             trackRepository.getNextFavoriteTracks(currentTracksCount)
        }
    }


     fun onAction(action: FavoriteTracksAction) {
        when (action) {
            is FavoriteTracksAction.SyncronizeTracks -> syncronizeTracks()
            is  FavoriteTracksAction.GetNextFavoriteTracks -> loadNextFavoriteTracks()
            is FavoriteTracksAction.onRemoveTrackById -> removeTrackGlobally(action.trackId)
            is FavoriteTracksAction.onRemoveTrackLocally -> removeTrackLocally(action.track)
            is FavoriteTracksAction.onRestoreLastRemovedTrackLocally -> restoreLastRemovedTrackLocally()
        }
    }


    private fun syncronizeTracks() {
        viewModelScope.launch {
            trackRepository.synchronizeTracks()
        }
    }
}
