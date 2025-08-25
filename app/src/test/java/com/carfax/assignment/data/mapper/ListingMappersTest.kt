package com.carfax.assignment.data.mapper

import com.carfax.assignment.data.remote.dto.DealerDto
import com.carfax.assignment.data.remote.dto.ImagesDto
import com.carfax.assignment.data.remote.dto.ListingDto
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ListingMappersTest {

    @Test
    fun `resolveId prefers VIN then id then vdpUrl then hash`() {
        val dtoVin = ListingDto(
            id = null, vin = "1HGCM82633A123456", year = 2020, make = "Honda",
            model = "Civic", trim = "EX", mileage = 10000, price = 19999.0,
            dealer = DealerDto(city = "Austin", state = "TX", phone = "5125551234"),
            images = ImagesDto(medium = listOf(), large = listOf(), firstPhoto = null),
            exteriorColor = null, interiorColor = null, driveType = null,
            transmission = null, engine = null, bodyStyle = null, vdpUrl = null, stockNumber = null
        )
        val e1 = dtoVin.toEntity()
        assertThat(e1.id).isEqualTo("1HGCM82633A123456")

        val dtoId = dtoVin.copy(vin = null, id = "server-id-123")
        val e2 = dtoId.toEntity()
        assertThat(e2.id).isEqualTo("server-id-123")

        val dtoUrl = dtoVin.copy(vin = null, id = null, vdpUrl = "https://example.com/veh/abc")
        val e3 = dtoUrl.toEntity()
        assertThat(e3.id).isEqualTo("https://example.com/veh/abc")

        val dtoHash = dtoVin.copy(vin = null, id = null, vdpUrl = null, year = 2018, make = "Ford", model = "Fusion")
        val e4 = dtoHash.toEntity()
        assertThat(e4.id).isNotEmpty()
    }

    @Test
    fun `photoUrl picks first available size`() {
        val dto = ListingDto(
            id = "x", vin = null, year = 2020, make = "Honda", model = "Civic", trim = "EX",
            mileage = 1, price = 1.0,
            dealer = DealerDto(city = null, state = null, phone = null),
            images = ImagesDto(
                large = listOf("L1"),
                medium = emptyList(),
                firstPhoto = null
            ),
            exteriorColor = null, interiorColor = null, driveType = null,
            transmission = null, engine = null, bodyStyle = null, vdpUrl = null, stockNumber = null
        )
        val e = dto.toEntity()
        assertThat(e.photoUrl).isEqualTo("L1")
    }
}
