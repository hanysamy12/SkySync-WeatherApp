package com.example.skysync.repo

import com.example.skysync.data.remote.WeatherRemoteDataSource
import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse

class WeatherRepositoryImp(private val remoteDataSource: WeatherRemoteDataSource) :
    WeatherRepository{
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        language: String
    ): CurrentWeatherResponse {
       return remoteDataSource.getCurrentWeather(lat,lon,language="en")
    }

    override suspend fun getForecast(
        lat: Double,
        lon: Double,
        language: String
    ): ForecastWeatherResponse {
        return remoteDataSource.getForecast(lat,lon,language="en")
    }
}