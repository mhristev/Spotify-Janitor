package org.internship.kmp.martin.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import org.internship.kmp.martin.data.repository.TrackRepository

class BrowseTracksViewModel(private val trackRepository: TrackRepository): ViewModel() {
    fun searchTracks(query: String): Flow<List<String>> {
        return trackRepository.searchTracks(query)
    }
}