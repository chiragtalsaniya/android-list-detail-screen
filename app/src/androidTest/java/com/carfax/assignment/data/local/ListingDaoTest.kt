package com.carfax.assignment.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListingDaoTest {

    private lateinit var db: CarfaxDatabase
    private lateinit var dao: ListingDao

    @Before fun setUp() {
        db = androidx.room.Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CarfaxDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.listingDao()
    }

    @After fun tearDown() { db.close() }

    @Test fun upsert_and_observe() = runBlocking {
        val entity = ListingEntity(
            id = "1HGCM82633A123456",
            title = "2020 Honda Civic EX",
            price = 21499.0,
            mileage = 18750,
            city = "Austin", state = "TX", phone = "5125551234",
            photoUrl = "https://example.com/img.jpg",
            exteriorColor = "Blue", interiorColor = "Black",
            driveType = "FWD", transmission = "CVT",
            engine = "4 Cyl", bodyStyle = "Sedan"
        )
        dao.upsertAll(listOf(entity))
        val items = dao.observeAll().first()
        assertThat(items).hasSize(1)
        assertThat(items.first().id).isEqualTo("1HGCM82633A123456")
    }
}
