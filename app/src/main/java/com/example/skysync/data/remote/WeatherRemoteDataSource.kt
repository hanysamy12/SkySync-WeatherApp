package com.example.skysync.data.remote

import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat: Long?, lon: Long?, language: String, unit: String): Flow<CurrentWeatherResponse>
    suspend fun getForecast(lat: Long?, lon: Long?, language: String, unit: String): Flow<ForecastWeatherResponse>

}