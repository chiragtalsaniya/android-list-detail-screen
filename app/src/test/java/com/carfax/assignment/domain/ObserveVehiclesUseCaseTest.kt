package com.carfax.assignment.domain

import app.cash.turbine.test
import com.carfax.assignment.domain.model.Vehicle
import com.carfax.assignment.domain.repository.ListingsRepository
import com.carfax.assignment.domain.usecase.ObserveVehiclesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.google.common.truth.Truth.assertThat

private class FakeRepo : ListingsRepository {
    private val flow = MutableStateFlow<List<Vehicle>>(emptyList())
    override fun observeVehicles(): Flow<List<Vehicle>> = flow
    override fun observeVehicle(id: String): Flow<Vehicle?> = flow.map { list -> list.firstOrNull { it.id == id } }
    override suspend fun refresh() {}
    override suspend fun refreshWithRx() {}

    fun emit(items: List<Vehicle>) { flow.value = items }
}

class ObserveVehiclesUseCaseTest {
    @Test
    fun `emits repository vehicles`() = runTest {
        val repo = FakeRepo()
        val uc = ObserveVehiclesUseCase(repo)

        val v = Vehicle(
            id = "VIN1",
            title = "2020 Honda Civic EX",
            price = 21499.0,
            mileage = 18750,
            city = "Austin",
            state = "TX",
            phone = "5125551234",
            photoUrl = "https://example.com/1.jpg",
            exteriorColor = null, interiorColor = null,
            driveType = null, transmission = null,
            engine = null, bodyStyle = null
        )

        uc().test {
            assertThat(awaitItem()).isEmpty()
            repo.emit(listOf(v))
            val next = awaitItem()
            assertThat(next).hasSize(1)
            assertThat(next.first().id).isEqualTo("VIN1")
            cancelAndIgnoreRemainingEvents()
        }
    }
}
