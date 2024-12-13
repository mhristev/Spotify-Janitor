package org.internship.kmp.martin.track.presentation.browse_tracks

import androidx.compose.runtime.mutableStateOf
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.update
import org.internship.kmp.martin.core.domain.onError
import org.internship.kmp.martin.core.domain.onSuccess
import org.internship.kmp.martin.track.domain.Track
import org.internship.kmp.martin.track.data.repository.TrackRepository

class BrowseTracksViewModel(private val trackRepository: TrackRepository): ViewModel() {
    private val _state = MutableStateFlow(BrowseTracksState())

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
            is BrowseTracksAction.OnErrorMessageShown -> setErrorString(null)
            BrowseTracksAction.OnSaveSuccessShown -> setIsSavingToFavoritesSuccessful(false)
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
            setIsSavingToFavoritesSuccessful(true)
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
        _state.update {
            it.copy(isSavingToFavoritesSuccess = isSavingSuccessful)
        }
    }

    private fun setErrorString(errorStr: String?) {
        _state.update {
            it.copy(errorString = errorStr)
        }
    }

}