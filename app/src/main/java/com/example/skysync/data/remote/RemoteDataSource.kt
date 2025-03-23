package com.example.skysync.data.remote

import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse

class WeatherRemoteDataSourceImp private constructor(val service: ApiService) :
    WeatherRemoteDataSource {
    override suspend fun getCurrentWeather(
        lat: Long?,
        lon: Long?,
        language: String,
        unit: String
    ): CurrentWeatherResponse {
        return service.getCurrentWeather(lat, lon, language =language, unit = unit)
    }

    override suspend fun getForecast(
        lat: Long?,
        lon: Long?,
        language: String,
        unit: String
    ): ForecastWeatherResponse {
        return service.getForecast(lat, lon, language =language, unit = unit)

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