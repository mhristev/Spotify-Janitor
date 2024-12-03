package org.internship.kmp.martin.track.presentation.fav_tracks_list


import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.data.repository.TrackRepository


class FavoriteTracksViewModel(private val trackRepository: TrackRepository): ViewModel() {
    private val _state = MutableStateFlow(FavoriteTracksState())

    private val limitTracksPerPage = AppConstants.Limits.MIN_TRACKS_TO_DISPLAY

    @NativeCoroutinesState
    val state = _state
        .onStart {
            loadFavoriteTracks()
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
    private fun loadNextFavoriteTracks() {
        viewModelScope.launch {
            val currentTracksCount = _state.value.tracks.size
            if (currentTracksCount >= 1000) return@launch

            val result = trackRepository.getNextFavoriteTracks(currentTracksCount)
            if (result is Result.Success) {
                _state.update { currentState ->
                    val resData = result.data.firstOrNull() ?: emptyList()
                    currentState.copy(tracks = currentState.tracks + resData)
                }
            }
        }
    }

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
            FavoriteTracksAction.GetNextFavoriteTracks -> loadNextFavoriteTracks()
        }
    }


//    fun observeFavoriteTracks() {
//        viewModelScope.launch {
//            val result = trackRepository.getFavoriteTracks()
//            if (result is Result.Success) {
//                _state.update { it.copy(tracks = result.data) }
//            }
//        }
//    }
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
        val currentTracksCount = _state.value.tracks.size
        viewModelScope.launch {
            when (val result = trackRepository.synchronizeTracks()) {
                is Result.Success -> {
                    result.data.collect { collectedTracks ->
                        _state.update { it.copy(tracks = collectedTracks.sortedByDescending { it.addedAt }.take(currentTracksCount)) }
                    }
                }
                is Result.Error -> {
                    // Handle the error appropriately
                }
            }
        }
    }
}