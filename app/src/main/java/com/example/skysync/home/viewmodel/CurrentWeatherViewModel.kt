package com.example.skysync.home.viewmodel

interface CurrentWeatherViewModel {
     fun loadInitialValues(lat: Double?,lon: Double?): Triple<String, String, String>

}