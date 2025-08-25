package com.carfax.assignment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ListingEntity::class], version = 1, exportSchema = false)
abstract class CarfaxDatabase : RoomDatabase() {
    abstract fun listingDao(): ListingDao
}