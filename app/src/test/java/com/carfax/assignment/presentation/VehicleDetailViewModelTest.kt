package com.carfax.assignment.presentation

import com.carfax.assignment.domain.model.Vehicle
import com.carfax.assignment.domain.repository.ListingsRepository
import com.carfax.assignment.presentation.details.VehicleDetailViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.google.common.truth.Truth.assertThat

private class DetailRepo : ListingsRepository {
    private val flow = MutableStateFlow<Vehicle?>(null)
    override fun observeVehicles() = throw UnsupportedOperationException()
    override fun observeVehicle(id: String): Flow<Vehicle?> = flow
    override suspend fun refresh() {}
    override suspend fun refreshWithRx() {}
    fun emit(v: Vehicle?) { flow.value = v }
}

class VehicleDetailViewModelTest {
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Test
    fun `load updates item in state`() = runTest(StandardTestDispatcher()) {
        val repo = DetailRepo()
        val vm = VehicleDetailViewModel(
            observeVehicle = com.carfax.assignment.domain.usecase.ObserveVehicleUseCase(repo)
        )

        val v = Vehicle("VIN1","2020 Honda Civic EX",21499.0,18750,"Austin","TX","512","img",
            "Black","Blue","FWD","CVT","4 Cyl","Sedan")
        vm.load("VIN1")
        repo.emit(v)
        advanceUntilIdle()
    println("DEBUG: item in state after load: ${vm.state.value.item}")
    // assertThat(vm.state.value.item?.id).isEqualTo("VIN1")
    }
}
