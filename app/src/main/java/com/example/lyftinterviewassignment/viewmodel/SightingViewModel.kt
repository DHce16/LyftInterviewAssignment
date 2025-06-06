package com.example.lyftinterviewassignment.viewmodel

// SightingViewModel.kt
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.lyftinterviewassignment.model.SightingType
import com.example.lyftinterviewassignment.model.UFOSighting
import com.example.lyftinterviewassignment.state.SightingUiState


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SightingViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<SightingUiState>(SightingUiState.Loading)
    val uiState: StateFlow<SightingUiState> = _uiState.asStateFlow()

    private val sightings = mutableListOf<UFOSighting>()

    init {
        _uiState.value = SightingUiState.Success(sightings)
    }

    fun addRandomSighting() {
        val newSighting = UFOSighting(
            id = System.currentTimeMillis(),
            date = LocalDate.now(),
            type = if (Random.nextBoolean()) SightingType.BLOB else SightingType.LAMPSHADE,
            speed = Random.nextDouble(100.0, 1000.0)
        )
        sightings.add(newSighting)
        _uiState.value = SightingUiState.Success(sightings.toList())
    }

    fun removeSighting(id: Long) {
        sightings.removeAll { it.id == id }
        _uiState.value = SightingUiState.Success(sightings.toList())
    }
}
