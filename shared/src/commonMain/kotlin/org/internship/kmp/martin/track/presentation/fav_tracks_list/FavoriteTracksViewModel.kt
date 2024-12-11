package org.internship.kmp.martin.track.presentation.fav_tracks_list

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.core.domain.Result
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
//            val bufferSize = AppConstants.Limits.TRACKS_PER_LOAD_MORE
//            val localTracksCount = trackRepository.getLocalFavoriteTracksCount()
//            if (localTracksCount - _state.value.tracks.size <= bufferSize) {
//                trackRepository.fetchAndStoreAdditionalTracks(localTracksCount)
//            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )
    private fun loadFavoriteTracks() {
        viewModelScope.launch {
            val result = trackRepository.getFavoriteTracks()
            if (result is Result.Success) {
                _state.update {
                    it.copy(
                        tracks = result.data.sortedByDescending {it.addedAt }.take(limitTracksPerPage)
                    )
                }
            }
        }

    }

    private fun removeTrackFromCashedList(track: Track) {
        _state.update { currentState ->
            val index = currentState.tracks.indexOfFirst { it.id == track.id }
            currentState.copy(
                tracks = currentState.tracks.filter { it.id != track.id },
                lastRemovedTrack = track,
                lastRemovedTrackIndex = index
            )
        }
    }

    private fun restoreLastRemovedTrackToCashedList() {
        val track = _state.value.lastRemovedTrack ?: return
        val index = _state.value.lastRemovedTrackIndex ?: return
        _state.update {
            val updatedTracks = it.tracks.toMutableList()
            updatedTracks.add(index, track)
            it.copy(tracks = updatedTracks, lastRemovedTrack = null, lastRemovedTrackIndex = null)
        }
    }

    private fun removeTrackById(id: String) {
        viewModelScope.launch {
            val track = _state.value.tracks.find { it.id == id } ?: _state.value.lastRemovedTrack ?: return@launch
            trackRepository.removeFavoriteTrack(track)
        }
    }

//    private fun loadNextFavoriteTracks() {
//        viewModelScope.launch {
//            val currentTracksCount = _state.value.tracks.size
//            if (currentTracksCount >= 1000) return@launch
//
//            val result = trackRepository.getNextFavoriteTracks(currentTracksCount)
//            if (result is Result.Success) {
//                _state.update { currentState ->
//                    val resData = result.data.firstOrNull() ?: emptyList()
//                    currentState.copy(tracks = currentState.tracks + resData)
//                }
//            } else if (result is Result.Error) {
//                _state.update { currentState ->
//
//                    currentState.copy(errorString = result.error.toString())
//                }
//            }
//        }
//    }
    private fun loadNextFavoriteTracks() {

        viewModelScope.launch {
            val currentTracksCount = _state.value.tracks.size
            if (currentTracksCount >= 1000) return@launch

            val result = trackRepository.getNextFavoriteTracks(currentTracksCount)

            if (result is Result.Success) {
                val tracks = result.data.firstOrNull() ?: emptyList()
                _state.update { currentState ->
                    currentState.copy(tracks = currentState.tracks + tracks)
                }

//                val bufferSize = AppConstants.Limits.TRACKS_PER_LOAD_MORE
//                val localTracksCount = trackRepository.getLocalFavoriteTracksCount()
//                if (localTracksCount - currentTracksCount <= bufferSize) {
//                    trackRepository.fetchAndStoreAdditionalTracks(localTracksCount)
//                }
            } else if (result is Result.Error) {
                _state.update { currentState ->

                    currentState.copy(errorString = result.error.toString())
                }
            }

        }
    }


    fun onAction(action: FavoriteTracksAction) {

        when (action) {
//            is FavoriteTracksAction.onTrackDelete -> removeTrack(action.track)
//            is FavoriteTracksAction.onUndoDeleteTrack -> {
//                val track = _state.value.lastRemovedTrack ?: return
//
//                _state.update { currentState ->
//                    currentState.copy(tracks = listOf(track) + currentState.tracks, lastRemovedTrack = null)
//                }
//                undoRemoveTrack(track)
//            }
            is FavoriteTracksAction.SyncronizeTracks -> syncronizeTracks()
            is  FavoriteTracksAction.GetNextFavoriteTracks -> loadNextFavoriteTracks()
            is FavoriteTracksAction.onRemoveTrackById -> removeTrackById(action.trackId)
            is FavoriteTracksAction.onRemoveTrackFromCashedList -> removeTrackFromCashedList(action.track)
            is FavoriteTracksAction.onRestoreLastRemovedTrackToCashedList -> restoreLastRemovedTrackToCashedList()
        }
    }

//    private fun removeTrack(track: Track) {
//        viewModelScope.launch {
//            _state.update { currentState ->
//                val updatedTracks = currentState.tracks.filter { it.id != track.id }
//                currentState.copy(tracks = updatedTracks)
//            }
//            trackRepository.removeFavoriteTrack(track)
//        }
//    }

//    private fun undoRemoveTrack(track: Track) {
//        viewModelScope.launch {
//            trackRepository.addFavoriteTrack(track)
//        }
//    }

    private fun syncronizeTracks() {
        val currentTracksCount = _state.value.tracks.size
        viewModelScope.launch {
            when (val result = trackRepository.synchronizeTracks()) {
                is Result.Success -> {
                    result.data.collect { collectedTracks ->
                        _state.update { it.copy(tracks = collectedTracks.sortedByDescending { it.addedAt }.take(currentTracksCount)) }
                    }
                }
                is Result.Error -> {
                }
            }
        }
    }
}
