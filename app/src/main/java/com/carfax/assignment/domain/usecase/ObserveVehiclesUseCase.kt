package com.carfax.assignment.domain.usecase

import com.carfax.assignment.domain.repository.ListingsRepository

class ObserveVehiclesUseCase(private val repo: ListingsRepository) {
    operator fun invoke() = repo.observeVehicles()
}