package com.example.skysync.data.remote

import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import org.intellij.lang.annotations.Language

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat : Double,lon: Double,language: String,unit: String): CurrentWeatherResponse
    suspend fun getForecast(lat : Double,lon: Double,language: String,unit: String): ForecastWeatherResponse

}