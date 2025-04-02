package com.example.skysync.repo

import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.models.SearchLocationsResponseItem
import com.example.skysync.models.StoredLocation
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getFavoriteLocations(): Flow<List<StoredLocation>>
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
    suspend fun adNewFavoriteLocations(storedLocation: StoredLocation): Long
    suspend fun deleteFavoriteLocation(storedLocation: StoredLocation): Int

}