package com.example.skysync.home.viewmodel

interface CurrentWeatherViewModel {
     fun loadInitialValues(): Triple<String, String, String>
     fun loadFavoriteInitialValues( lat: Double,lon: Double): Triple<String, String, String>

}