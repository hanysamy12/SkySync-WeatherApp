package com.example.skysync.models

data class CurrentWeatherResponse(
    val visibility: Int? = null,
    val timezone: Int? = null,
    val main: Main? = null,
    val clouds: Clouds? = null,
    val sys: SysCurrent? = null,  //Sys
    val dt: Int? = null,
    val coord: Coord? = null,
    val weather: List<WeatherItem?>? = null,
    val name: String? = null,
    val cod: Int? = null,
    val id: Int? = null,
    val base: String? = null,
    val wind: Wind? = null
)


data class SysCurrent(  //Sys
    val country: String? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null
)




