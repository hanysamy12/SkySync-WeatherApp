package com.example.skysync.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationsDAO {
    @Query("SELECT * FROM locations")
    fun getAllLocations() : Flow<List<StoredLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(storedLocation: StoredLocation): Long

    @Delete
    suspend fun deleteLocation(storedLocation: StoredLocation) : Int
}