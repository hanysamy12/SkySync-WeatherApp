package com.example.skysync.home.viewmodel

interface CurrentWeatherViewModel {
     fun getCurrentWeather(lang: String,unit: String)
     fun getForecast(lang: String,unit: String)
}