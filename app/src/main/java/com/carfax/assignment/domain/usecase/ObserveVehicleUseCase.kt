package com.carfax.assignment.domain.usecase

import com.carfax.assignment.domain.repository.ListingsRepository

class ObserveVehicleUseCase(private val repo: ListingsRepository) {
    operator fun invoke(id: String) = repo.observeVehicle(id)
}