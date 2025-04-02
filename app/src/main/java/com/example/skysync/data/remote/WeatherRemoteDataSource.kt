package com.example.skysync.data.remote

import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.models.SearchLocationsResponseItem
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat: Double?, lon: Double?, language: String, unit: String): Flow<CurrentWeatherResponse>
    suspend fun getForecast(lat: Double?, lon: Double?, language: String, unit: String): Flow<ForecastWeatherResponse>
    suspend fun searchForLocation(searchQuery : String) : Flow<List<SearchLocationsResponseItem>>
}