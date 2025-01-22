package org.internship.kmp.martin.track.presentation.fav_tracks_list

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.internship.kmp.martin.core.domain.onError
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.data.repository.TrackRepository

sealed class UIEvent {
    data class ShowError(val message: String) : UIEvent()
    data class ShowSuccess(val message: String) : UIEvent()
}


open class FavoriteTracksViewModel(private val trackRepository: TrackRepository) : ViewModel() {

    private val trackToDelete = MutableStateFlow<Track?>(null)
    private val _uiEvents = MutableSharedFlow<UIEvent>(replay = 0)
    private val limitFlow = MutableStateFlow(0)
    private val _state = MutableStateFlow(FavoriteTracksState())
    private val isLoadingSync = MutableStateFlow(false)
    private val isLoadingNextTracks = MutableStateFlow(false)

    @NativeCoroutines
    val uiEvents = _uiEvents.asSharedFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val cachedTracks = limitFlow
        .flatMapLatest { limit ->
            trackRepository.getFavoriteTracksFlow(limit)
        }

    @NativeCoroutinesState
    val state =
        combine(
            cachedTracks,
            trackToDelete,
            limitFlow,
            isLoadingSync,
            isLoadingNextTracks,
        ) { tracks, trackToDelete, limitFlow, isLoadingSync, isLoadingNextTracks ->

            FavoriteTracksState(
                cachedTracks = tracks,
                isShowingUndoButton = trackToDelete != null,
                isLoadingSync = isLoadingSync,
                isLoadingNextTracks = isLoadingNextTracks
            )
        }
        .onStart {
            loadNextFavoriteTracks()
            syncAllTracks()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000L),
            _state.value
        )

    fun onAction(action: FavoriteTracksAction) {
        when (action) {
            is FavoriteTracksAction.OnSyncTracks -> syncAllTracks()
            is FavoriteTracksAction.OnGetNextFavoriteTracks -> loadNextFavoriteTracks()
            is FavoriteTracksAction.OnRemoveTrackByIdGlobally -> removeTrackGlobally(action.trackId)
            is FavoriteTracksAction.OnRemoveTrackLocally -> removeTrackLocally(action.track)
            is FavoriteTracksAction.OnRestoreLastRemovedTrackLocally -> restoreLastRemovedTrackLocally()
        }
    }

    private fun loadNextFavoriteTracks() {
        viewModelScope.launch {
            isLoadingSync.value = true
            val desiredLimit = limitFlow.value + 50

            trackRepository.checkAndFetchFavoriteTracks(currentTrackCount = limitFlow.value, increaseWith = 50)
                .onError { setErrorMessage(it.toString()) }

            limitFlow.value = desiredLimit
            isLoadingSync.value = false
        }
    }

    private fun removeTrackLocally(track: Track) {
        viewModelScope.launch {
            trackToDelete.value = track
            trackRepository.removeFavoriteTrackLocally(track)
        }
    }

    private fun restoreLastRemovedTrackLocally() {
        val track = trackToDelete.value ?: return
        viewModelScope.launch {
            trackRepository.restoreTrackLocally(track)
            trackToDelete.value = null
        }
    }

    private fun removeTrackGlobally(id: String) {
        viewModelScope.launch {
            val track = trackToDelete.value?.takeIf { it.id == id } ?: return@launch
            trackRepository.removeFavoriteTrackGlobally(track)
                .onSuccess {
                    trackToDelete.value = null
                }
                .onError {
                    setErrorMessage(it.toString())
                }
        }
    }

    private fun syncAllTracks() {
        viewModelScope.launch {
            isLoadingSync.value = true
            trackRepository.syncAllTracks()
                .onError {
                    setErrorMessage(it.toString())
                }
            isLoadingSync.value = false
        }
    }

    private fun setErrorMessage(errorStr: String?) {
        if (errorStr != null) {
            viewModelScope.launch {
                _uiEvents.emit(UIEvent.ShowError(errorStr))
            }
        }
    }
}
