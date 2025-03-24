package com.example.skysync.repo

import com.example.skysync.data.remote.WeatherRemoteDataSource
import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImp(private val remoteDataSource: WeatherRemoteDataSource) :
    WeatherRepository {
    override suspend fun getCurrentWeather(
        lat: Long?,
        lon: Long?,
        language: String,
        unit: String
    ): Flow<CurrentWeatherResponse> {
        return remoteDataSource.getCurrentWeather(lat, lon, language,  unit)
    }

    override suspend fun getForecast(
        lat: Long?,
        lon: Long?,
        language: String,
        unit: String
    ): Flow<ForecastWeatherResponse> {
        return remoteDataSource.getForecast(lat, lon, language , unit)
    }
}