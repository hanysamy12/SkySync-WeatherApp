package com.example.skysync

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

class Constants{
    companion object{
        const val REQUEST_PERMEATION_CODE = 1999
        const val LOCATION_DATASTORE_NAME = "location_store"
        const val SETTINGS_DATASTORE_NAME = "settings_store"
        val CURRENT_LAT_KEY = longPreferencesKey("current_latitude")
        val CURRENT_LON_KEY = longPreferencesKey("current_longitude")
        val LANGUAGE_KEY = stringPreferencesKey("selected_language")
        val LOCATION_WAY_KEY =stringPreferencesKey("location_way")
        val TEMPERATURE_UNIT =stringPreferencesKey("temperature_measure")
        val WIND_SPEED_UNIT= stringPreferencesKey("wind_speed_measure")


    }
}
sealed class Response<out T>{
    data object Loading : Response<Nothing>()
    data class Success<T> (val data : T): Response<T>()
    data class Failure (val error: Throwable): Response<Nothing>()
}