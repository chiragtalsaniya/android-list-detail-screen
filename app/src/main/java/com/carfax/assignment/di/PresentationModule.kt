package com.carfax.assignment.di

import com.carfax.assignment.domain.usecase.ObserveVehicleUseCase
import com.carfax.assignment.domain.usecase.ObserveVehiclesUseCase
import com.carfax.assignment.domain.usecase.RefreshVehiclesUseCase
import com.carfax.assignment.presentation.details.VehicleDetailViewModel
import com.carfax.assignment.presentation.listings.ListingsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    factoryOf(::ObserveVehiclesUseCase)
    factoryOf(::ObserveVehicleUseCase)
    factoryOf(::RefreshVehiclesUseCase)

    viewModelOf(::ListingsViewModel)       // constructor (ObserveVehiclesUseCase, RefreshVehiclesUseCase) will be injected
    viewModelOf(::VehicleDetailViewModel)  // constructor (ObserveVehicleUseCase) will be injected
}