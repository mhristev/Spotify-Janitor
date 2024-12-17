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
    private val _state = MutableStateFlow(FavoriteTracksState())

    private val limitFlow = MutableStateFlow(0)

    private val _uiEvents = MutableSharedFlow<UIEvent>(replay = 0)
    @NativeCoroutines
    val uiEvents = _uiEvents.asSharedFlow()

    @NativeCoroutinesState
    val state = _state
        .onStart {
            loadNextFavoriteTracks()
            observeFavoriteTracks()
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
            is FavoriteTracksAction.OnHideUndoOption -> setShowingUndoButton(false)
            is FavoriteTracksAction.OnHideDeletionDialog -> setShowingDeleteConfirmation(false)
            is FavoriteTracksAction.OnShowDeletionDialog -> setShowingDeleteConfirmation(true)
        }
    }
    /* This function observes the favorite tracks and updates the state
    limitFlow is the upstream flow. Whenever limitFlow emits a new integer (e.g., when you increase your limit from 50 to 100),
    flatMapLatest cancels the old inner flow and starts collecting a new one returned by getFavoriteTracksFlow(newLimit).

    getFavoriteTracksFlow(limit) returns a Flow<List<Track>> that continuously emits updates as data changes in the database.
    As long as limitFlow doesn't change, flatMapLatest will keep collecting from the current getFavoriteTracksFlow(limit) and will receive all new data emitted by that flow.
    */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeFavoriteTracks() {
        viewModelScope.launch {
            limitFlow
                .flatMapLatest { limit ->
                    trackRepository.getFavoriteTracksFlow(limit)
                }
                .collect { tracks ->
                    setCashedTracksFlow(tracks)
                }
        }
    }

    private fun loadNextFavoriteTracks() {
        viewModelScope.launch {
            setLoadingGetTracks(true)
            val desiredLimit = limitFlow.value + 50

            trackRepository.getNextFavoriteTracks(currentTrackCount = limitFlow.value, desiredIncreaseWith = 50)
                .onError { setErrorMessage(it.toString()) }

            limitFlow.value = desiredLimit
            setLoadingGetTracks(false)
        }
    }

    private fun removeTrackLocally(track: Track) {
        viewModelScope.launch {
            setTrackToDelete(track)
            trackRepository.removeFavoriteTrackLocally(track)
            setShowingUndoButton(true)
        }
    }

    private fun restoreLastRemovedTrackLocally() {
        val track = _state.value.trackToDelete ?: return
        viewModelScope.launch {
            trackRepository.restoreTrackLocally(track)
            setTrackToDelete(null)
            setShowingUndoButton(false)
        }
    }

    private fun removeTrackGlobally(id: String) {
        viewModelScope.launch {
            val track = _state.value.trackToDelete?.takeIf { it.id == id } ?: return@launch
            trackRepository.removeFavoriteTrackGlobally(track)
                .onSuccess {
                    setTrackToDelete(null)
                }
                .onError {
                    setErrorMessage(it.toString())
                }
        }
    }

    private fun syncAllTracks() {
        viewModelScope.launch {
            setLoadingSync(true)
            trackRepository.syncAllTracks()
                .onError {
                    setErrorMessage(it.toString())
                }
            setLoadingSync(false)
        }
    }

    private fun setTrackToDelete(track: Track?) {
        _state.update {
            it.copy(trackToDelete = track)
        }
    }

    private fun setCashedTracksFlow(tracks: List<Track>) {
        _state.update {
            it.copy(cashedTracks = tracks)
        }
    }

    private fun setLoadingSync(isLoading: Boolean) {
        _state.update {
            it.copy(isLoadingSync = isLoading)
        }
    }

    private fun setLoadingGetTracks(isLoading: Boolean) {
        _state.update {
            it.copy(isLoadingGetTracks = isLoading)
        }
    }

    private fun setShowingDeleteConfirmation(isShowing: Boolean) {
        _state.update {
            it.copy(isShowingDeleteConfirmation = isShowing)
        }
    }

    private fun setShowingUndoButton(isShowing: Boolean) {
        _state.update {
            it.copy(isShowingUndoButton = isShowing)
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
