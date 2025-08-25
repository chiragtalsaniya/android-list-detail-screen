package com.carfax.assignment.domain.model

data class Vehicle(
    val id: String,
    val title: String,
    val price: Double?, val mileage: Long?,
    val city: String?, val state: String?,
    val phone: String?, val photoUrl: String?,
    val exteriorColor: String?, val interiorColor: String?,
    val driveType: String?, val transmission: String?,
    val engine: String?, val bodyStyle: String?
)