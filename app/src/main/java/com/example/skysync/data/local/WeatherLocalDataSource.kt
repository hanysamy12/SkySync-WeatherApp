package com.example.skysync.data.local

import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun getAllLocations(): Flow<List<StoredLocation>>
    suspend fun insertLocation (storedLocation: StoredLocation): Long
    suspend fun deleteLocation (storedLocation: StoredLocation) : Int
}