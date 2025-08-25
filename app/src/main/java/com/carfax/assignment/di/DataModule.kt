package com.carfax.assignment.di

import androidx.room.Room
import com.carfax.assignment.data.local.CarfaxDatabase
import com.carfax.assignment.data.remote.CarfaxApi
import com.carfax.assignment.data.repository.ListingsRepositoryImpl
import com.carfax.assignment.domain.repository.ListingsRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val dataModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    single {
        Retrofit.Builder()
            .baseUrl("https://carfax-for-consumers.firebaseio.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(get())
            .build()
    }
    single { get<Retrofit>().create(CarfaxApi::class.java) }
    single {
        Room.databaseBuilder(
            androidContext(),
            CarfaxDatabase::class.java,
            "carfax_assignment_ct.db"
        ).build()
    }
    single { get<CarfaxDatabase>().listingDao() }
    single<ListingsRepository> { ListingsRepositoryImpl(get(), get()) }
}