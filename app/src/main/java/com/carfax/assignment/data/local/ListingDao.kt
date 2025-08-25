package com.carfax.assignment.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {
    @Query("SELECT * FROM listings ORDER BY title ASC")
    fun observeAll(): Flow<List<ListingEntity>>
    @Query("SELECT * FROM listings WHERE id = :id")
    fun observeById(id: String): Flow<ListingEntity?>
    @Query("SELECT * FROM listings ORDER BY title ASC")
    fun observeAllRx(): Flowable<List<ListingEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ListingEntity>)
}