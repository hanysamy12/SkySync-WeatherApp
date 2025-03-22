package com.example.skysync.home.viewmodel

interface CurrentWeatherViewModel {
     fun getCurrentWeather(lat: Double,lon: Double,lang: String,unit: String)
     fun getForecast(lat: Double,lon: Double,lang: String,unit: String)
}