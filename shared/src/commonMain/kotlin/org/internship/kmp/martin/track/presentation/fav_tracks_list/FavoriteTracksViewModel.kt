package org.internship.kmp.martin.track.presentation.fav_tracks_list

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import org.internship.kmp.martin.core.domain.onError
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.data.repository.TrackRepository


class FavoriteTracksViewModel(private val trackRepository: TrackRepository): ViewModel() {
    private val _state = MutableStateFlow(FavoriteTracksState())

    @NativeCoroutinesState
    val state = _state
        .onStart {
            loadNextFavoriteTracks()
            syncAllTracks()
        }
        .stateIn (
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: FavoriteTracksAction) {
        when (action) {
            is FavoriteTracksAction.OnSyncTracks -> syncAllTracks()
            is  FavoriteTracksAction.OnGetNextFavoriteTracks -> loadNextFavoriteTracks()
            is FavoriteTracksAction.OnRemoveTrackByIdGlobally -> removeTrackGlobally(action.trackId)
            is FavoriteTracksAction.OnRemoveTrackLocally -> removeTrackLocally(action.track)
            is FavoriteTracksAction.OnRestoreLastRemovedTrackLocally -> restoreLastRemovedTrackLocally()
            is FavoriteTracksAction.OnHideUndoOption -> setShowingUndoButton(false)
            is FavoriteTracksAction.OnHideDeletionDialog -> setShowingDeleteConfirmation(false)
            is FavoriteTracksAction.OnShowDeletionDialog -> setShowingDeleteConfirmation(true)
            is FavoriteTracksAction.OnErrorMessageShown -> setErrorMessage(null)
        }
    }

    private fun loadNextFavoriteTracks() {
        viewModelScope.launch {
            setLoadingGetTracks(true)
            val tracksCount = _state.value.cashedTracksFlow.firstOrNull()?.size ?: 0
            trackRepository.getNextFavoriteTracks(tracksCount)
                .onSuccess { tracksFlow ->
                    setCashedTracksFlow(tracksFlow)
                }
                .onError {
                    setErrorMessage(it.toString())
                }
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

    private fun setCashedTracksFlow(tracksFlow: Flow<List<Track>>) {
        _state.update {
            it.copy(cashedTracksFlow = tracksFlow)
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
        _state.update {
            it.copy(errorString = errorStr)
        }
    }

}
