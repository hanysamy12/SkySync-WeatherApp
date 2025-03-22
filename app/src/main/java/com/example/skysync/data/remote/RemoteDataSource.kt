package com.example.skysync.data.remote

import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse

class WeatherRemoteDataSourceImp private constructor(val service: ApiService) :
    WeatherRemoteDataSource {
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        language: String
    ): CurrentWeatherResponse {
        return service.getCurrentWeather(lat, lon)
    }

    override suspend fun getForecast(
        lat: Double,
        lon: Double,
        language: String
    ): ForecastWeatherResponse {
        return service.getForecast(lat, lon)
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