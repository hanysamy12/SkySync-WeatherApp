package com.example.skysync.repo

import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Long?, lon: Long?, language: String, unit: String): CurrentWeatherResponse
    suspend fun getForecast(lat: Long?, lon: Long?, language: String, unit: String): ForecastWeatherResponse
}