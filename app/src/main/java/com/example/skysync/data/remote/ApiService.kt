package com.example.skysync.data.remote

import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object RetrofitHelper {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val retrofitInstance =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    val retrofitService: ApiService = retrofitInstance.create(ApiService::class.java)
}

interface ApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") appId: String = "4d8ffcfda7b01ee2b930a3cd193273e4",
        @Query("lang") language: String = "en",
        @Query("units") unit: String
    ): CurrentWeatherResponse

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") appId: String = "4d8ffcfda7b01ee2b930a3cd193273e4",
        @Query("lang") language: String = "en",
        @Query("units") unit: String
    ): ForecastWeatherResponse
}