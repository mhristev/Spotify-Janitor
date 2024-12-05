package org.internship.kmp.martin.track.presentation.browse_tracks

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.update
import org.internship.kmp.martin.core.domain.DataError
import org.internship.kmp.martin.core.domain.Result
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
        }
    }

    @Throws(DataError::class)
    private fun searchTracks(query: String) {
        viewModelScope.launch {
            val results = trackRepository.searchTracks(query)
            if (results is Result.Success) {
                _state.update {
                    it.copy(
                        tracks = results.data
                    )
                }
            } else {
                results.onError {
                    throw it
                }
            }
        }
    }
    @Throws(DataError::class)
    private fun addTrackToFavorites(track: Track) {
        viewModelScope.launch {
            val result = trackRepository.addFavoriteTrack(track)
            result.onError {
                throw it
            }
        }
    }
}