package org.internship.kmp.martin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.internship.kmp.martin.core.domain.Result
import org.internship.kmp.martin.data.domain.Track
import org.internship.kmp.martin.data.repository.TrackRepository
import org.internship.kmp.martin.presentation.fav_tracks_list.FavoriteTracksState

class BrowseTracksViewModel(private val trackRepository: TrackRepository): ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    @NativeCoroutinesState
    var cashedTracks: MutableStateFlow<List<Track>> = MutableStateFlow(emptyList())

    private val _state = MutableStateFlow(FavoriteTracksState())

    fun searchTracks(query: String) {
        viewModelScope.launch {
            val results = trackRepository.searchTracks(query)
            if (results is Result.Success) {
                cashedTracks.value = results.data
            }
        }

    }
}