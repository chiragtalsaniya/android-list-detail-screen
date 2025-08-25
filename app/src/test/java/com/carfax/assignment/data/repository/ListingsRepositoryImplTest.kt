package com.carfax.assignment.data.repository

import com.carfax.assignment.data.local.ListingDao
import com.carfax.assignment.data.local.ListingEntity
import com.carfax.assignment.data.remote.CarfaxApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.rx3.asFlowable

private class FakeDao : ListingDao {
    val captured = mutableListOf<ListingEntity>()
    private val state = MutableStateFlow<List<ListingEntity>>(emptyList())

    override suspend fun upsertAll(items: List<ListingEntity>) { captured.addAll(items) }
    override fun observeAll() = throw UnsupportedOperationException()
    override fun observeById(id: String): Flow<ListingEntity?> =
        state.map { list -> list.firstOrNull { it.id == id } }

    override fun observeAllRx(): Flowable<List<ListingEntity>> =
        state.asFlowable()

}

class ListingsRepositoryImplTest {

    private lateinit var server: MockWebServer
    private lateinit var api: CarfaxApi
    private lateinit var dao: FakeDao
    private lateinit var repo: ListingsRepositoryImpl

    @Before fun setUp() {
        server = MockWebServer().apply { start() }
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(CarfaxApi::class.java)

        dao = FakeDao()
        repo = ListingsRepositoryImpl(api, dao, io = Dispatchers.IO)
    }

    @After fun tearDown() { server.shutdown() }

    @Test fun `refresh inserts entities (coroutines path)`() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(200).setBody(sampleJson))
        repo.refresh()
        assertThat(dao.captured).isNotEmpty()
    }

    @Test fun `refreshWithRx inserts entities (rx path)`() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(200).setBody(sampleJson))
        repo.refreshWithRx()
        assertThat(dao.captured).isNotEmpty()
    }
}

private const val sampleJson = """{
  "listings": [
    {
      "id": "id1",
      "vin": "VIN1",
      "year": 2020,
      "make": "Honda",
      "model": "Civic",
      "trim": "EX",
      "mileage": 18750,
      "currentPrice": 21499.0,
      "dealer": {
        "city": "Austin",
        "state": "TX",
        "phone": "5125551234"
      },
      "images": {
        "medium": ["https://example.com/medium.jpg"],
        "large": ["https://example.com/large.jpg"],
        "firstPhoto": {
          "small": "https://example.com/small.jpg",
          "medium": "https://example.com/medium.jpg",
          "large": "https://example.com/large.jpg"
        }
      },
      "exteriorColor": "Blue",
      "interiorColor": "Black",
      "drivetype": "FWD",
      "transmission": "CVT",
      "engine": "4 Cyl",
      "bodytype": "Sedan",
      "vdpUrl": "https://example.com/veh/abc",
      "stockNumber": "STK123"
    }
  ]
}
"""
