package com.example.skysync.settings.viewmodel


interface SettingsViewModel {
    fun setLanguage(language : String)
    fun setLocationWay(locationWay:String)
    fun setTempUnit(temperatureMeasure: String)
    fun setWindUnit(windMeasure : String)
}