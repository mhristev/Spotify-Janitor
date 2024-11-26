package org.internship.kmp.martin.presentation.fav_tracks_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.data.repository.TrackRepository
import org.internship.kmp.martin.presentation.TrackListState


class FavoriteTracksViewModel(private val trackRepository: TrackRepository): ViewModel() {

    @NativeCoroutinesState
    var cashedTracks: MutableStateFlow<List<Track>> = MutableStateFlow(emptyList())

    var lastRemovedTrack = mutableStateOf<Track?>(null)
    private var observeFavoriteJob: Job? = null

//
//    private val _state = MutableStateFlow(TrackListState())
//
//    val state = _state.asStateFlow()
//        .onStart {
//            if(cashedTracks.value.isEmpty()) {
////                observeSearchQuery()
//            }
//            observeFavoriteTracks()
//        }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000L),
//            _state.value
//        )

//
//    fun onAction(action: FavTracksAction) {
//        when (action) {
//            is FavTracksAction.onTrackDelete -> removeTrack(action.)
//            is FavTracksAction.onUndoDeleteTrack -> undoRemoveTrack()
//            is FavTracksAction.SyncronizeTracks -> syncronizeTracks()
//        }
//    }

    fun observeFavoriteTracks() {
        viewModelScope.launch {
            val result = trackRepository.getFavoriteTracks()
            if (result is Result.Success) {
                cashedTracks.value = result.data
            }
        }
    }

    fun addTrackToFavorites(track: Track) {
        viewModelScope.launch { trackRepository.addFavoriteTrack(track) }
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

    fun removeTrack(track: Track) {
        viewModelScope.launch {
            trackRepository.removeFavoriteTrack(track)
                .onSuccess {
                    cashedTracks.value = cashedTracks.value.filter { it.id != track.id }
                }
            lastRemovedTrack.value = track
        }
    }

    fun undoRemoveTrack() {
        lastRemovedTrack?.let { track ->
            viewModelScope.launch {
                trackRepository.addFavoriteTrack(track.value!!)
                lastRemovedTrack.value = null
            }
        }
    }

}