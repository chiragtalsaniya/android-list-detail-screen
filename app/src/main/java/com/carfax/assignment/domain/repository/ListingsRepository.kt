package com.carfax.assignment.domain.repository

import com.carfax.assignment.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

interface ListingsRepository {
    fun observeVehicles(): Flow<List<Vehicle>>
    fun observeVehicle(id: String): Flow<Vehicle?>
    suspend fun refresh()
    suspend fun refreshWithRx()
}