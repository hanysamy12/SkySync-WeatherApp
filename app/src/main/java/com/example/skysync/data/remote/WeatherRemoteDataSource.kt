package com.example.skysync.data.remote

import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat: Long?, lon: Long?, language: String, unit: String): CurrentWeatherResponse
    suspend fun getForecast(lat: Long?, lon: Long?, language: String, unit: String): ForecastWeatherResponse


}