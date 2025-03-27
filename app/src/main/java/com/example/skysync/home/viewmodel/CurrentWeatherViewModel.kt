package com.example.skysync.home.viewmodel

interface CurrentWeatherViewModel {
     fun loadInitialValues(): Triple<String, String, String>

}