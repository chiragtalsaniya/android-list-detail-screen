package com.carfax.assignment

import android.app.Application
import com.carfax.assignment.di.dataModule
import com.carfax.assignment.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CarfaxApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CarfaxApp)
            modules(dataModule, presentationModule)
        }
    }
}