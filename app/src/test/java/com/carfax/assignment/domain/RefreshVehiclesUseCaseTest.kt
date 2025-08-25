package com.carfax.assignment.domain

import com.carfax.assignment.domain.repository.ListingsRepository
import com.carfax.assignment.domain.usecase.RefreshVehiclesUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.google.common.truth.Truth.assertThat

private class CaptureRepo : ListingsRepository {
    var refreshCalled = false
    var refreshRxCalled = false
    override fun observeVehicles() = throw UnsupportedOperationException()
    override fun observeVehicle(id: String) = throw UnsupportedOperationException()
    override suspend fun refresh() { refreshCalled = true }
    override suspend fun refreshWithRx() { refreshRxCalled = true }
}

class RefreshVehiclesUseCaseTest {
    @Test
    fun `calls coroutine refresh when useRx is false`() = runTest {
        val repo = CaptureRepo()
        val uc = RefreshVehiclesUseCase(repo)
        uc(useRx = false)
        assertThat(repo.refreshCalled).isTrue()
        assertThat(repo.refreshRxCalled).isFalse()
    }

    @Test
    fun `calls rx refresh when useRx is true`() = runTest {
        val repo = CaptureRepo()
        val uc = RefreshVehiclesUseCase(repo)
        uc(useRx = true)
        assertThat(repo.refreshCalled).isFalse()
        assertThat(repo.refreshRxCalled).isTrue()
    }
}
