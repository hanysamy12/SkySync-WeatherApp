package com.example.skysync.data.remote

import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.models.SearchLocationsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRemoteDataSourceImp private constructor(val service: ApiService) :
    WeatherRemoteDataSource {
    override suspend fun getCurrentWeather(
        lat: Double?,
        lon: Double?,
        language: String,
        unit: String
    ): Flow<CurrentWeatherResponse> {
        val weather = service.getCurrentWeather(lat, lon, language =language, unit = unit)
        return flowOf( weather)
    }

    override suspend fun getForecast(
        lat: Double?,
        lon: Double?,
        language: String,
        unit: String
    ): Flow<ForecastWeatherResponse >{
        val forecast = service.getForecast(lat, lon, language =language, unit = unit)
        return  flowOf(forecast)
    }

    override suspend fun searchForLocation(searchQuery: String): Flow<SearchLocationsResponse> {
        val searchResult = service.getGeocode(searchQuery)
        return flowOf(searchResult)
    }

    companion object {
        var INSTANCE: WeatherRemoteDataSource? = null
        fun getInstance(): WeatherRemoteDataSource {
            return INSTANCE ?: synchronized(this) {
                val temp = WeatherRemoteDataSourceImp(RetrofitHelper.retrofitService)
                INSTANCE = temp
                temp
            }
        }
    }
}