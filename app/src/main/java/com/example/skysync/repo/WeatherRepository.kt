package com.example.skysync.repo

import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse

interface WeatherRepository {
    suspend fun getCurrentWeather(lat : Double,lon : Double,language: String): CurrentWeatherResponse
    suspend fun getForecast(lat : Double,lon : Double,language: String): ForecastWeatherResponse

}