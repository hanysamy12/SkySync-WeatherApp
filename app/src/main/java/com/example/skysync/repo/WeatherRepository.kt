package com.example.skysync.repo

import com.example.skysync.models.Alert
import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.models.SearchLocationsResponseItem
import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface WeatherRepository {

    suspend fun getCurrentWeather(
        lat: Double?,
        lon: Double?,
        language: String,
        unit: String
    ): Flow<CurrentWeatherResponse>

    suspend fun getForecast(
        lat: Double?,
        lon: Double?,
        language: String,
        unit: String
    ): Flow<ForecastWeatherResponse>
    suspend fun searchLocation(searchQuery: String): Flow<List<SearchLocationsResponseItem>>
    fun getFavoriteLocations(): Flow<List<StoredLocation>>
    suspend fun adNewFavoriteLocations(storedLocation: StoredLocation): Long
    suspend fun deleteFavoriteLocation(storedLocation: StoredLocation): Int
    fun getAlerts(): Flow<List<Alert>>
    suspend fun adNewAlert(alert: Alert): Long
    suspend fun deleteAlert(alertId: UUID): Int

}