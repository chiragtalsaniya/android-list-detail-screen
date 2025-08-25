package com.carfax.assignment.data.repository

import com.carfax.assignment.data.local.ListingDao
import com.carfax.assignment.data.mapper.toDomain
import com.carfax.assignment.data.mapper.toEntity
import com.carfax.assignment.data.remote.CarfaxApi
import com.carfax.assignment.domain.model.Vehicle
import com.carfax.assignment.domain.repository.ListingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ListingsRepositoryImpl(
    private val api: CarfaxApi,
    private val dao: ListingDao,
    private val io: CoroutineDispatcher = Dispatchers.IO
) : ListingsRepository {

    override fun observeVehicles(): Flow<List<Vehicle>> =
        dao.observeAll().map { it.map { e -> e.toDomain() } }

    override fun observeVehicle(id: String): Flow<Vehicle?> =
        dao.observeById(id).map { it?.toDomain() }

    override suspend fun refreshWithRx() = withContext(io) {
        val entities = api.getListingsRx()
            .map { res -> res.listings.map { it.toEntity() } }
            .blockingGet()
        dao.upsertAll(entities)
    }

    override suspend fun refresh() = withContext(io) {
        val entities = api.getListingsCo().listings.map { it.toEntity() }
        dao.upsertAll(entities)
    }
}