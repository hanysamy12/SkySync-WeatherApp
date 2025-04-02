package com.example.skysync.data.local

import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalDataSource(private val locations : MutableList<StoredLocation> =mutableListOf()): WeatherLocalDataSource {
    private val locationsFlow = MutableStateFlow<List<StoredLocation>>(locations.toList())
    override fun getAllLocations(): Flow<List<StoredLocation>> {
        return locationsFlow
    }

    override suspend fun insertLocation(storedLocation: StoredLocation): Long {
        locations.add(storedLocation)
        locationsFlow.value =locations.toList()
        return locations.lastIndex.toLong()
    }

    override suspend fun deleteLocation(storedLocation: StoredLocation): Int {
        val removed = locations.remove(storedLocation)
        locationsFlow.value = locations.toList() // Update the flow
        return if (removed) 1 else 0
    }
}