package com.carfax.assignment.presentation.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carfax.assignment.domain.model.Vehicle
import com.carfax.assignment.domain.usecase.ObserveVehiclesUseCase
import com.carfax.assignment.domain.usecase.RefreshVehiclesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListingsViewModel(
    private val observeVehicles: ObserveVehiclesUseCase,
    private val refreshVehicles: RefreshVehiclesUseCase
) : ViewModel() {

    data class UiState(
        val items: List<Vehicle> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeVehicles().collect { items ->
                _state.update { it.copy(items = items, isLoading = false, error = null) }
            }
        }
    }

    fun refresh(useRx: Boolean = true) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { refreshVehicles(useRx) }
                .onFailure {
                    _state.update { s ->
                        s.copy(
                            isLoading = false,
                            error = "Couldn’t refresh listings. Check connection."
                        )
                    }
                }
        }
    }
}