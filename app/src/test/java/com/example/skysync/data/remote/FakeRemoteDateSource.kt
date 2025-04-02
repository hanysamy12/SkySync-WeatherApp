package com.example.skysync.data.remote

import com.example.skysync.models.City
import com.example.skysync.models.Coord
import com.example.skysync.models.CurrentWeatherResponse
import com.example.skysync.models.ForecastWeatherResponse
import com.example.skysync.models.ListItem
import com.example.skysync.models.Main
import com.example.skysync.models.SearchLocationsResponseItem
import com.example.skysync.models.WeatherItem
import kotlinx.coroutines.flow.flowOf

class FakeRemoteDateSource(): WeatherRemoteDataSource {
    var currentWeatherResponse = CurrentWeatherResponse(
        weather = listOf(WeatherItem("Clear", "clear sky")),
        main = Main(temp = 22.0, humidity = 50),
        name = "Test City"
    )

    var forecastResponse = ForecastWeatherResponse(
        city = City(
            name = "Test City",
            coord = Coord(lon = 0.0, lat = 0.0)
        ),
        list = listOf(
            ListItem(
                dt = 212200,
                main = Main(temp = 22.0),
                weather = listOf(WeatherItem("Sunny", "clear sky")),
                dtTxt = "2023-10-01 12:00:00"
            )
        )
    )

    var searchResponse = listOf(
        SearchLocationsResponseItem(country = "Test Country" , "Test City", lat = 0.0, lon =  0.0, )
    )
    override suspend fun getCurrentWeather(
        lat: Double?,
        lon: Double?,
        language: String,
        unit: String
    ) = flowOf(currentWeatherResponse)

    override suspend fun getForecast(
        lat: Double?,
        lon: Double?,
        language: String,
        unit: String
    ) = flowOf(forecastResponse)

    override suspend fun searchForLocation(searchQuery: String)=flowOf(searchResponse)
}