package com.carfax.assignment.domain.usecase

import com.carfax.assignment.domain.repository.ListingsRepository

class RefreshVehiclesUseCase(private val repo: ListingsRepository) {
    suspend operator fun invoke(useRx: Boolean = true) =
        if (useRx) repo.refreshWithRx() else repo.refresh()
}