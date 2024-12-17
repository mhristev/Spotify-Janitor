package org.internship.kmp.martin.track.presentation.browse_tracks

import androidx.compose.runtime.mutableStateOf
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import org.internship.kmp.martin.core.domain.onError
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.data.repository.TrackRepository
import org.internship.kmp.martin.track.presentation.fav_tracks_list.UIEvent

open class BrowseTracksViewModel(private val trackRepository: TrackRepository): ViewModel() {
    private val _state = MutableStateFlow(BrowseTracksState())

    private val _uiEvents = MutableSharedFlow<UIEvent>(replay = 0)
    @NativeCoroutines
    val uiEvents = _uiEvents.asSharedFlow()

    @NativeCoroutinesState
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: BrowseTracksAction) {
        when(action) {
            is BrowseTracksAction.onTrackAddToFavorites -> addTrackToFavorites(action.track)
            is BrowseTracksAction.onSearch -> searchTracks(action.query)
        }
    }

    private fun searchTracks(query: String) {
        viewModelScope.launch {
            setIsLoadingTracks(true)
            trackRepository.searchTracks(query)
                .onSuccess { tracks ->
                    setTracks(tracks)
                }
                .onError {
                    setErrorString(it.toString())
                }
            setIsLoadingTracks(false)
        }
    }

    private fun addTrackToFavorites(track: Track) {
        viewModelScope.launch {
            trackRepository.addTrackToFavorites(track)
                .onError {
                    setErrorString(it.toString())
                }
                .onSuccess {
                    setIsSavingToFavoritesSuccessful(true)
                }
        }
    }

    private fun setTracks(resultTracks: List<Track>) {
        _state.update {
            it.copy(tracks = resultTracks)
        }
    }

    private fun setIsLoadingTracks(isLoading: Boolean) {
        _state.update {
            it.copy(isLoadingTracks = isLoading)
        }
    }

    private fun setIsSavingToFavoritesSuccessful(isSavingSuccessful: Boolean) {
        if (isSavingSuccessful) {
            viewModelScope.launch {
                _uiEvents.emit(UIEvent.ShowSuccess("Added to favorites!"))
            }
        }
    }

    private fun setErrorString(errorStr: String?) {
        if (errorStr != null) {
            viewModelScope.launch {
                _uiEvents.emit(UIEvent.ShowError(errorStr))
            }
        }
    }

}