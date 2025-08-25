package com.carfax.assignment.data.remote

import com.carfax.assignment.data.remote.dto.ListingResponseDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CarfaxApi {
    @GET("assignment.json")
    fun getListingsRx(): Single<ListingResponseDto>
    @GET("assignment.json")
    suspend fun getListingsCo(): ListingResponseDto
}