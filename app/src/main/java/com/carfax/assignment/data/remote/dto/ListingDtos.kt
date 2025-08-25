package com.carfax.assignment.data.remote.dto

import com.squareup.moshi.Json

data class ListingResponseDto(val listings: List<ListingDto> = emptyList())

data class ListingDto(
    val id: String?,
    val vin: String?,
    val year: Int?, val make: String?, val model: String?, val trim: String?,
    val mileage: Long?, @Json(name = "currentPrice") val price: Double?,
    val dealer: DealerDto?, val images: ImagesDto?,
    @Json(name = "exteriorColor") val exteriorColor: String?,
    @Json(name = "interiorColor") val interiorColor: String?,
    @Json(name = "drivetype") val driveType: String?,
    val transmission: String?, val engine: String?,
    @Json(name = "bodytype") val bodyStyle: String?,
    val vdpUrl: String?,
    val stockNumber: String? = null
)


data class DealerDto(val city: String?, val state: String?, val phone: String?)

data class ImagesDto(
    val medium: List<String>?, val large: List<String>?, val firstPhoto: FirstPhotoDto?
)

data class FirstPhotoDto(val small: String?, val medium: String?, val large: String?)