package com.example.lyftinterviewassignment.state

import com.example.lyftinterviewassignment.model.UFOSighting


sealed class SightingUiState {
    object Loading : SightingUiState()
    data class Success(val sightings: List<UFOSighting>) : SightingUiState()
    data class Error(val message: String) : SightingUiState()
}
