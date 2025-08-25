package com.carfax.assignment.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carfax.assignment.domain.model.Vehicle
import com.carfax.assignment.domain.usecase.ObserveVehicleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VehicleDetailViewModel(
    private val observeVehicle: ObserveVehicleUseCase
) : ViewModel() {

    data class UiState(val item: Vehicle? = null)

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch {
            observeVehicle(id).collect { v -> _state.update { it.copy(item = v) } }
        }
    }
}