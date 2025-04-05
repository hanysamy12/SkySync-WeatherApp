package com.example.skysync.data.local

import com.example.skysync.models.Alert
import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface WeatherLocalDataSource {
    fun getAllLocations(): Flow<List<StoredLocation>>
    suspend fun insertLocation (storedLocation: StoredLocation): Long
    suspend fun deleteLocation (storedLocation: StoredLocation) : Int
    fun getAlerts(): Flow<List<Alert>>
    suspend fun insertAlert (alert: Alert): Long
    suspend fun deleteAlert (alertId: UUID) : Int
}