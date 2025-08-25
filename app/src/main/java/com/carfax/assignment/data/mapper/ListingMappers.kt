package com.carfax.assignment.data.mapper

import com.carfax.assignment.data.local.ListingEntity
import com.carfax.assignment.data.remote.dto.ListingDto
import com.carfax.assignment.domain.model.Vehicle

private fun ListingDto.resolveId(): String {
    val primary = vin ?: id ?: vdpUrl
    if (!primary.isNullOrBlank()) return primary
    val raw = listOfNotNull(year?.toString(), make, model, trim, stockNumber)
        .joinToString("-")
    return if (raw.isNotBlank()) raw.hashCode().toString() else System.nanoTime().toString()
}

fun ListingDto.toEntity() = ListingEntity(
    id = resolveId(),
    title = listOfNotNull(year?.toString(), make, model, trim).joinToString(" "),
    price = price,
    mileage = mileage,
    city = dealer?.city, state = dealer?.state, phone = dealer?.phone,
    photoUrl = images?.firstPhoto?.medium ?: images?.medium?.firstOrNull()
    ?: images?.large?.firstOrNull(),
    exteriorColor = exteriorColor, interiorColor = interiorColor,
    driveType = driveType, transmission = transmission, engine = engine, bodyStyle = bodyStyle
)

fun ListingEntity.toDomain() = Vehicle(
    id, title, price, mileage, city, state, phone, photoUrl,
    exteriorColor, interiorColor, driveType, transmission, engine, bodyStyle
)